package cn.iocoder.yudao.module.system.dal.dataobject.regulation;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 规章制度表
 *
 * @author ruoyi
 */
@TableName("system_regulation")
@KeySequence("system_regulation_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class RegulationDO extends BaseDO {

    /**
     * 规章制度ID
     */
    private Long id;
    /**
     * 规章制度标题
     */
    private String title;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件URL
     */
    private String fileUrl;
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    /**
     * 状态（0草稿 1已发布 2已下架）
     */
    private Integer status;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

} 