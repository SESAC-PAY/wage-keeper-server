package org.sesac.wagekeeper.domain.document.dto.request;

public record HtmlRequestDto(String html) {
    public String getHtml() {
          return html;
    }
}
