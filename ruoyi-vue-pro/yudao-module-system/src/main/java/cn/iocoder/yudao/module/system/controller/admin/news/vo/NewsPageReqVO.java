package cn.iocoder.yudao.module.system.controller.admin.news.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 新闻分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsPageReqVO extends PageParam {

    @Schema(description = "新闻标题", example = "芋道")
    private String title;

    @Schema(description = "新闻类型", example = "1")
    private Integer type;

    @Schema(description = "新闻状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

} 