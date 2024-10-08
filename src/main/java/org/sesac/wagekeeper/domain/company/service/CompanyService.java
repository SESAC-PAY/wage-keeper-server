package org.sesac.wagekeeper.domain.company.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sesac.wagekeeper.domain.company.dto.response.CompanyInfoResponseDto;
import org.sesac.wagekeeper.domain.company.dto.response.CompanyLocationDto;
import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.company.repository.CompanyRepository;
import org.sesac.wagekeeper.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.sesac.wagekeeper.global.error.ErrorCode.COMPANY_IMAGE_NOT_FOUND;
import static org.sesac.wagekeeper.global.error.ErrorCode.COMPANY_NOT_FOUND;
import org.sesac.wagekeeper.domain.Util.Util;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.employmentinfo.repository.EmploymentInfoRepository;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;


import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
public class CompanyService {


    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final CompanyRepository companyRepository;
    private final EmploymentInfoRepository employmentInfoRepository;
    private final UserRepository userRepository;
    
    
    private static final String SARMIN_BASE_URL = "https://www.saramin.co.kr/zf_user/search/company";
    private static final String COMPANY_DETAIL_BASE_URL = "https://www.saramin.co.kr/zf_user/company-info/view";
    private static final String KAKAO_GEOCODING_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_REVERSE_GEOCODING_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";


    public CompanyInfoResponseDto getCompanyInfo(String searchWord) {
        CompanyInfoResponseDto companyInfo = crawlingCompanyInfo(searchWord);

        if (companyInfo.companyName().isBlank()) {
            throw new EntityNotFoundException(COMPANY_NOT_FOUND);
        }
        Company company = companyRepository.findByCompanyName(companyInfo.companyName()).orElseGet(() -> {
            Company newCompany = Company.builder()
                    .companyName(companyInfo.companyName())
                    .address(companyInfo.address())
                    .employer(companyInfo.employer())
                    .image(companyInfo.image())
                    .build();
            return companyRepository.save(newCompany);
        });

        return new CompanyInfoResponseDto(
                company.getId(),
                company.getCompanyName(),
                company.getAddress(),
                company.getEmployer(),
                company.getImage()
        );
    }



    // 회사 정보 크롤링(이름, 주소, 대표자명)
    public CompanyInfoResponseDto crawlingCompanyInfo(String searchWord) {
        String url = SARMIN_BASE_URL + "?searchword=" + searchWord + "&go=&flag=n&searchMode=1&searchType=search&search_done=y&search_optional_item=n";
        String name = null;
        String address = null;
        String employer = null;
        String csn = null; // 회사 이미지 검색용

        try {
            Document doc = Jsoup.connect(url).get();

            Elements companyLinks = doc.select("a.company_popup");
            if (!companyLinks.isEmpty()) {
                Element firstCompanyLink = companyLinks.first();
                name = firstCompanyLink.attr("title");
                String href = firstCompanyLink.attr("href");
                String prefix = "csn=";
                int index = href.indexOf(prefix);
                if (index != -1) {
                    csn = href.substring(index + prefix.length());
                }
            }

            Element firstCompany = doc.selectFirst("div.item_corp");
            if (firstCompany != null) {
                Elements dlElements = firstCompany.select("dl");
                for (Element dlElement : dlElements) {
                    Element dt = dlElement.selectFirst("dt");
                    Element dd = dlElement.selectFirst("dd");

                    if (dt != null && dd != null) {
                        String dtText = dt.text();
                        if (dtText.equals("기업주소")) {
                            address = dd.text();
                        } else if (dtText.equals("대표자명")) {
                            employer = dd.text();
                        }
                    }
                }
            } else {
                address = "회사를 찾을 수 없습니다.";
                employer = "회사를 찾을 수 없습니다.";
            }
        } catch (IOException e) {
            throw new EntityNotFoundException(COMPANY_NOT_FOUND);
        }

        String image = (csn != null) ? crawlingCompanyImage(csn) : null;

        return new CompanyInfoResponseDto(null,name, address, employer, image);
    }

    // 회사 이미지 검색 크롤링
    private String crawlingCompanyImage(String csn) {
        String detailUrl = COMPANY_DETAIL_BASE_URL + "?csn=" + csn;
        String image = null;

        try {
            Document doc = Jsoup.connect(detailUrl).get();
            Element logoDiv = doc.selectFirst("div.box_logo");
            if (logoDiv != null) {
                Element img = logoDiv.selectFirst("img");
                if (img != null) {
                    image = img.attr("src");
                }
            }
        } catch (IOException e) {
            throw new EntityNotFoundException(COMPANY_IMAGE_NOT_FOUND);
        }
        return image;
    }

    // 주소에서 위도, 경도 가져오기
    public CompanyLocationDto getLatLng(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlStr = KAKAO_GEOCODING_URL + "?query=" + encodedAddress;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlStr))
                    .header("Authorization", "KakaoAK " + kakaoApiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray documents = jsonObject.getAsJsonArray("documents");
                if (documents.size() > 0) {
                    JsonObject location = documents.get(0).getAsJsonObject();
                    double lat = location.get("y").getAsDouble();
                    double lng = location.get("x").getAsDouble();
                    return new CompanyLocationDto(lat, lng);
                }
            } else {
                System.out.println("카카오맵 API 호출 실패: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new EntityNotFoundException(COMPANY_NOT_FOUND);
        }
        return null;
    }

    // 위도, 경도에서 주소 가져오기
    public String getAddressFromLatLng(double latitude, double longitude) {
        try {
            String urlStr = KAKAO_REVERSE_GEOCODING_URL + "?x=" + longitude + "&y=" + latitude;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlStr))
                    .header("Authorization", "KakaoAK " + kakaoApiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray documents = jsonObject.getAsJsonArray("documents");
                if (documents.size() > 0) {
                    JsonObject address = documents.get(0).getAsJsonObject().getAsJsonObject("address");
                    return address.get("address_name").getAsString();
                }
            } else {
                System.out.println("카카오맵 API 호출 실패: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new EntityNotFoundException(COMPANY_NOT_FOUND);
        }
        return null;
    }

    public String getCompanyName(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(COMPANY_NOT_FOUND));
        return company.getCompanyName();
    }



    public void joinCompany(Long companyId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Company> company = companyRepository.findById(companyId);

        if(user.isEmpty() || company.isEmpty()) throw new RuntimeException("No User " + userId + " or Company " + companyId);

        employmentInfoRepository.save(
                EmploymentInfo.builder()
                        .user(user.get())
                        .company(company.get())
                        .salary(Util.WAGE_PER_HOUR)
                        .time(0L)
                        .build()
        );
    }

}
