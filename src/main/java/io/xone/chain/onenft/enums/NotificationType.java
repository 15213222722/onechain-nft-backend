package io.xone.chain.onenft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    USER_FOLLOW("您有新的粉丝", "用户 {0} 关注了您."),
    NFT_COLLECTED("NFT被收藏", "用户 {1}收藏了您的 NFT「{0}」"),
    NFT_SOLD("你的NFT已成交", "您的NFT「{0}」以{2} {3}价格出售给{1}"),
    NFT_BOUGHT("NFT 购买成功", "您以{2} {3}价格从{1}购买了NFT「{0}」"),
    NFT_SWAPPED("NFT交换成功", "您与{1}成功交换了NFT「{0}」"),;

    private final String titleTemplate;
    private final String contentTemplate;
    
    public String  formatTitle(Object... args) {
        // Simple formatting, could be enhanced
        return titleTemplate;
    }

    public String formatContent(Object... args) {
        String content = contentTemplate;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                content = content.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        return content;
    }
}