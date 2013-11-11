package com.example.EmotionChat;

/**
 * ひとつの発言に対応するデータクラス
 */
public class ChatItem {
    private final long ownerId;
    private final String content;

    public ChatItem(long ownerId, String content) {
        this.ownerId = ownerId;
        this.content = content;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public String getContent() {
        return content;
    }
}
