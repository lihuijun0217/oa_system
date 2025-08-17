package cn.iocoder.yudao.module.system.service.regulation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.regulation.RegulationDO;

import jakarta.validation.Valid;

/**
 * 规章制度 Service 接口
 */
public interface RegulationService {

    /**
     * 创建规章制度
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRegulation(@Valid RegulationSaveReqVO createReqVO);

    /**
     * 更新规章制度
     *
     * @param updateReqVO 更新信息
     */
    void updateRegulation(@Valid RegulationSaveReqVO updateReqVO);

    /**
     * 删除规章制度
     *
     * @param id 编号
     */
    void deleteRegulation(Long id);

    /**
     * 获得规章制度
     *
     * @param id 编号
     * @return 规章制度
     */
    RegulationDO getRegulation(Long id);

    /**
     * 获得规章制度分页
     *
     * @param pageReqVO 分页查询
     * @return 规章制度分页
     */
    PageResult<RegulationDO> getRegulationPage(RegulationPageReqVO pageReqVO);

    /**
     * 发布规章制度
     *
     * @param id 编号
     */
    void publishRegulation(Long id);

    /**
     * 下架规章制度
     *
     * @param id 编号
     */
    void unpublishRegulation(Long id);

    /**
     * 下载规章制度文件
     *
     * @param id 编号
     * @return 文件内容
     */
    byte[] downloadRegulation(Long id) throws Exception;

} 