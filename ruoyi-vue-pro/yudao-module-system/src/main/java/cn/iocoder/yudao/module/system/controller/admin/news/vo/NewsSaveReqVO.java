package cn.iocoder.yudao.module.system.controller.admin.news.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 新闻新增/修改 Request VO")
@Data
public class NewsSaveReqVO {

    @Schema(description = "新闻ID", example = "1024")
    private Long id;

    @Schema(description = "新闻标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    @NotEmpty(message = "新闻标题不能为空")
    private String title;

    @Schema(description = "新闻内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道源码")
    @NotEmpty(message = "新闻内容不能为空")
    private String content;

    @Schema(description = "新闻类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "新闻类型不能为空")
    private Integer type;

    @Schema(description = "图片URL", example = "https://www.iocoder.cn")
    private String imageUrl;

    @Schema(description = "新闻状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "新闻状态不能为空")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

} 