package cn.iocoder.yudao.module.bpm.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessChainDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 串联流程关系 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmProcessChainMapper extends BaseMapperX<BpmProcessChainDO> {

    /**
     * 根据源流程标识查询可发起的后续流程
     *
     * @param sourceProcessKey 源流程标识
     * @return 串联流程关系列表
     */
    default List<BpmProcessChainDO> selectBySourceProcessKey(String sourceProcessKey) {
        return selectList(BpmProcessChainDO::getSourceProcessKey, sourceProcessKey);
    }

    /**
     * 根据目标流程标识查询前置流程
     *
     * @param targetProcessKey 目标流程标识
     * @return 串联流程关系列表
     */
    default List<BpmProcessChainDO> selectByTargetProcessKey(String targetProcessKey) {
        return selectList(BpmProcessChainDO::getTargetProcessKey, targetProcessKey);
    }

} 