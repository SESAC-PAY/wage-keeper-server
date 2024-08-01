package org.sesac.wagekeeper.domain.Util;

public class GPTConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-4o";
    public static final Integer MAX_TOKEN = 1000;
    public static final Boolean STREAM = true;
    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";
    public static final Double TEMPERATURE = 0.7;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    public static final String RESPONSE_NODE_AT = "/choices/0/delta/content";

}
