package cn.iocoder.yudao.module.system.dal.dataobject.news;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 新闻表
 *
 * @author ruoyi
 */
@TableName("system_news")
@KeySequence("system_news_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsDO extends BaseDO {

    /**
     * 新闻ID
     */
    private Long id;
    /**
     * 新闻标题
     */
    private String title;
    /**
     * 新闻内容
     */
    private String content;
    /**
     * 新闻类型（1图片新闻 2文字新闻）
     */
    private Integer type;
    /**
     * 图片URL
     */
    private String imageUrl;
    /**
     * 新闻状态（0草稿 1已发布 2已下架）
     */
    private Integer status;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

} 