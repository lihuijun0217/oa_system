package cn.iocoder.yudao.module.system.controller.admin.news.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 新闻 Response VO")
@Data
public class NewsRespVO {

    @Schema(description = "新闻ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "新闻标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String title;

    @Schema(description = "新闻内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道源码")
    private String content;

    @Schema(description = "新闻类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "图片URL", example = "https://www.iocoder.cn")
    private String imageUrl;

    @Schema(description = "新闻状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

} 