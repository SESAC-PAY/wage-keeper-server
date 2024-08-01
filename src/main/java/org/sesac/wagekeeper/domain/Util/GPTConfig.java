package org.sesac.wagekeeper.domain.Util;

import java.time.LocalDateTime;

public class GPTConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-4o";
    public static final Integer MAX_TOKEN = 2048;
    public static final Boolean STREAM = true;
    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";
    public static final Double TEMPERATURE = 0.7;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    public static final String RESPONSE_NODE_AT = "/choices/0/delta/content";

    public static final String[] basicQuestion = {
        "너는 다음 <> 사이의 내용을 더하거나 빼지 말고 그대로 출력해야 해. <와 >는 출력하지 마. <안녕하세요 저는 당신을 위한 임금지키미예요. 임금 체불 진정서를 저와 대화를 하면서 작성해보아요. \n" +
                "\"진정 이유\" 부분을 작성할 때 중요한 것은 사건의 배경과 구체적인 피해 사실을 명확히 하는 것입니다. 먼저, 언제부터 언제까지 어느 회사에서 일하셨는지 알려주실 수 있나요?>",

        "너는 다음 <> 사이의 내용을 더하거나 빼지 말고 그대로 출력해야 해. <와 >는 출력하지 마. <피해 사실에 대한 내용들 잘 들었습니다.  이제 조사관이 피해 사실을 파악할 때 제출 내용이 사실이라고 판단할 근거가 있어야 해요! \n" +
                "1. 고용 계약서\n" +
                "2. 임금 체불을 요구했던사장님과의 메시지 (카카오톡 등) 캡쳐본 \n" +
                "3. 통장 거래내역 >",

        "너는 다음 <> 사이의 내용을 더하거나 빼지 말고 그대로 출력해야 해. <진정서 작성을 위해 이름과 전화번호, 주소지, 주민번호를 알려주세요>",

        "너는 다음 <> 사이의 내용을 더하거나 빼지 말고 그대로 출력해야 해. <신고할 대상인 사장님의 이름과 전화번호를 알려주세요>"
    };

    public static final String[][] targetInfos = {
        {"재직 기간", "현재 재직중인지 여부", "퇴사일(퇴직한 경우)", "근로 계약서에 작성된 임금", "임금을 못 받은 기간", "못 받은 임금의 추산치", "처음에는 사장이 돈을 잘 줬는지", "입사일", "업무 내용", "퇴사를 하게 된 이유(퇴직한 경우)"},
        {"고용 계약서", "임금 체불을 요구한 사장님과의 메시지", "통장 거래 내역"},
        {"사용자 본인 이름", "전화번호", "주소지", "주민등록번호"},
        {"신고 대상자의 이름", "신고 대상자의 전화번호", "회사 이름", "회사 전화번호", "근무지 주소"},
        {"<html><head><meta charset='UTF-8'><style>body { font-family: 'Malgun Gothic', sans-serif; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh; } .container { border: 2px solid black; padding: 20px; max-width: 100%; box-sizing: border-box; min-height: 29.7cm; width: 21cm; /* A4 Size */ } h1 { text-align: center; margin-bottom: 10px; } h2 { margin-top: 30px; } h3 { margin-top: 20px; } p { margin: 5px 0; } .center { text-align: center; } .margin-top-40 { margin-top: 40px; } hr.double { border: 0; border-top: 1.5px solid black; width: 100%; margin-bottom: 0 auto; } .date { text-align: center; margin-top: 40px; } .bottom-right { text-align: right; margin-top: 20px; }</style></head><body><div class='container'><h1>진정서</h1><hr class='double'><h2>제목 : ${제목}</h2><h3>진정인 정보</h3><p><strong>성명:</strong> ${진정인 성명}</p><p><strong>주민번호:</strong> ${진정인 주민번호}</p><p><strong>주소:</strong> ${진정인 주소}</p><p><strong>전화번호:</strong> ${진정인 전화번호}</p><h3>피진정인 정보</h3><p><strong>성명:</strong> ${피진정인 성명}</p><p><strong>전화번호:</strong> ${피진정인 전화번호}</p><p><strong>주소:</strong> ${피진정인 주소}</p><h3 class='center margin-top-40'>- 진정취지 -</h3><p>${진정취지}</p><h3 class='center'>- 진정 이유 -</h3><p>${진정 이유}</p><h3 class='center margin-top-40'>- 증거자료 -</h3><p>${증거자료}</p><div class='date'><p>${년}년 ${월}월 ${일}일</p></div><div class='bottom-right'><hr class='double'><p>진정인: ${진정인 성명} (인)</p></div></div></body></html>"}
    };

    private static final String[] systemPrompts = {

        """
        너는 대한민국에서 일을 하고 있는 외국인 노동자와 대화를 하는 챗봇이야.
        챗봇의 사용자는 대한민국에서 일을 하고 있는 외국인 노동자야.
        너의 임무는 사용자와 자연스럽게 대화하면서 사용자에게서 정보를 알아내는 거야.
        너가 알아내야 할 정보는 system prompt 가장 아래에 기재되어 있어.
        일부 정보는 특정한 조건에서만 필요한데, 조건은 정보(조건) 다음과 같이 괄호 안에 기재되어 있어.
        따라서 괄호가 뒤에 붙어 있는 정보를 얻기 위해선 괄호 안에 있는 조건을 미리 확인해야 해.
        만약 조건을 만족하는지 만족하지 못하는지 알 수 없다면 조건을 만족하는지 검사하기 위한 질문을 먼저 해야해.
        만약 모든 정보를 획득했다면 응답의 마지막에 "좋아요. 다음으로 넘어갑시다"를 붙여줘.
        모든 정보를 획득하지 못했다면 어떤 경우라도 "좋아요. 다음으로 넘어갑시다"를 사용해선 안되.
        """,

        """
        너는 대한민국에서 일을 하고 있는 외국인 노동자와 대화를 하는 챗봇이야.
        챗봇의 사용자는 대한민국에서 일을 하고 있는 외국인 노동자야.
        너의 임무는 사용자와 자연스럽게 대화하면서 사용자에게서 정보를 알아내는 거야.
        너가 알아내야 할 정보는 system prompt 가장 아래에 기재되어 있어.
        한 정보에 대해 사용자가 여러번 정보를 줄 수 도 있어.
        따라서 주어진 항목의 정보가 모두 입력 됐더라도 사용자가 입력할 정보가 남아있을 수 있어.
        따라서 모든 항목의 정보가 입력 됐다면 사용자에게 "좋아요 더 추가할 자료가 있나요?" 라고 물어본 후 사용자가 없다는 의사를 표현하면 "좋아요. 다음으로 넘어갑시다"라고 응답해. 
        모든 정보를 획득하지 못했다면 어떤 경우라도 "좋아요. 다음으로 넘어갑시다"를 사용해선 안되.
        "좋아요 더 추가할 자료가 있나요?" 이외의 다른 질문은 하지 말고 필요할 경우 적절한 대답만 해.
        """,

        """
        너는 대한민국에서 일을 하고 있는 외국인 노동자와 대화를 하는 챗봇이야.
        챗봇의 사용자는 대한민국에서 일을 하고 있는 외국인 노동자야.
        너의 임무는 사용자와 자연스럽게 대화하면서 사용자에게서 정보를 알아내는 거야.
        너가 알아내야 할 정보는 system prompt 가장 아래에 기재되어 있어.
        만약 모든 정보를 획득했다면 응답의 마지막에 "좋아요. 다음으로 넘어갑시다"를 붙여줘.
        모든 정보를 획득하지 못했다면 어떤 경우라도 "좋아요. 다음으로 넘어갑시다"를 사용해선 안되.
        """,

        """
        너는 대한민국에서 일을 하고 있는 외국인 노동자와 대화를 하는 챗봇이야.
        챗봇의 사용자는 대한민국에서 일을 하고 있는 외국인 노동자야.
        너의 임무는 사용자와 자연스럽게 대화하면서 사용자에게서 정보를 알아내는 거야.
        너가 알아내야 할 정보는 system prompt 가장 아래에 기재되어 있어.
        만약 모든 정보를 획득했다면 응답의 마지막에 "좋아요. 다음으로 넘어갑시다"를 붙여줘.
        모든 정보를 획득하지 못했다면 어떤 경우라도 "좋아요. 다음으로 넘어갑시다"를 사용해선 안되.
        """,

        """
        너는 주어진 채팅 정보를 가지고 가장 하단에 주어진 진정서 HTML을 완성하는 임무를 가지고 있어.
        작성하는 글들은 진정서의 분위기와 양식에 알맞아야 해.
        너는 ${}에서 '{' 와 '}' 사이에 어떤 정보를 넣어야 하는지 적혀 있어.
        ${태그 이름} 전체를 채팅 내역에서 추출한 정보로 치환하면 되.
        이외의 부분은 절대로 수정해서는 안되.
        너가 치환해야 하는 정보는 다음과 같아.
        'ex) <내용>' 이 부분은 예시인데 형식만 비슷하게 하고, 내용은 이전 채팅 내용을 기반으로 해야 해. 
        
        ${제목} : 진정서의 제목. ex) <비즈마켓 임금 미지급에 대한 건>
        ${진정인 성명} : 진정인의 이름. ex) <김재훈>
        ${진정인 주민번호} : 진정인의 주민등록 번호. ex) <010000-1000000>
        ${진정인 주소} : 진정인의 주소. ex) <서울시 종로구 숭인동>
        ${진정인 전화번호} : 진정인의 전화번호. ex) <010-1234-5678>
        ${피진정인 성명} : 피진정인의 이름. 즉 사장님의 이름. ex) <송하연>
        ${피진정인 전화번호} : 피진정인의 전화번호. ex) <010-9876-5432>
        ${회사 주소} : 근무했던 회사 주소. ex) <서울시 강남구>
        ${진정취지} : 진정서를 작성하는 간략한 이유. ex) <비즈마켓 측에서 3개월의 급여 6,000,000원(매월 2,000,000)원을 미지급하고 있는 바, 이를 엄중하게 조사하시어 미지급된 체불 임금을 진정인에게 즉시 지급 청산하도록 조치하여 주시기를 바라와 이에 진정합니다.>
        ${진정 이유} : 피진정인이 어떤 회사의 대표인지에 대한 소개, 진정인이 입사한 날짜와 퇴사한 날짜, 진정인이 퇴사하게 된 이유, 회사의 부정사항, 금액을 지급하지 않기 시작한 기간 등의 진정서와 관련된 내용을 진정서의 형식에 맞게 종합하여 작성한 진정인의 진정 이유. 적절하게 문단을 나눠서 만들어줘.
        ex) <피진정인은 위 주소지의 비즈마켓을 운영/관리하고 전국의 50개의 체인점을 소유하고 있는 대표이사이며, 진정인은 피진정이 운영하고 있는 비즈마켓에 2005년 3월 9일에 입사하여 근무하였고, 2009년 3월 5일에 퇴사한 근로자 입니다. 
        
        
        진정인은 피진정인의 회사에 입사하여 성실히 근무하였으나, 퇴사하기 6개월 전부터 회사는 어려움을 호소하며 임금을 지급하지 않았고, 그로 인해 진정인은 피로와 스트레스로 인한 질환을 얻게 되어 2009년 3월 5일 사직을 하게 되었습니다.
        
        
        피진정인은 임금을 지급하지 않은 3개월 동안 회사의 체인점을 20여개 업체로 증가하였음에도 불구하고, 회사의 자금 사정을 호소하며 사직한 지 2개월이 지난 지금까지도 3개월치의 임금을 지급하지 않았습니다. 또한 수 차례 피진정인에게 3개월치에 대한 임금과 퇴직금을 요청하였으나 무시하는 등 방관하여 3회에 걸친 내용 증명 발송 및 수 차례 독촉 전화에도 오히려 협박성 발언을 행하며 임금에 대한 지급의사를 표시하지 않았습니다. 이를 충분히 조사하시어 피진정인이 진정인에게 체불임금과 퇴직금을 지급할 수 있도록 선처하여 주시기 바랍니다.
         
        ${년} : 진정서를 작성하는 년도. ex) 2024
        ${월} : 진정서를 작성하는 월. ex) 8
        ${일} : 진정서를 작성하는 일자. ex) 1
        
        """
    };

    public static String getSystemPrompts(int level, boolean isFirst) {

        if(isFirst) return basicQuestion[level];

        StringBuilder targets = new StringBuilder();
        for(String curr:targetInfos[level]) {
            targets.append(curr);
        }
        return systemPrompts[level] + targets.toString() + "현재 날짜 : " + LocalDateTime.now();
    }

}
