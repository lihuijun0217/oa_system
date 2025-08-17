package cn.iocoder.yudao.module.system.service.regulation;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.regulation.RegulationDO;
import cn.iocoder.yudao.module.system.dal.mysql.regulation.RegulationMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.REGULATION_NOT_FOUND;

/**
 * 规章制度 Service 实现类
 *
 * @author ruoyi
 */
@Service
@Validated
public class RegulationServiceImpl implements RegulationService {

    @Resource
    private RegulationMapper regulationMapper;

    @Override
    public Long createRegulation(RegulationSaveReqVO createReqVO) {
        // 插入
        RegulationDO regulation = BeanUtils.toBean(createReqVO, RegulationDO.class);
        // 如果状态是已发布，设置发布时间
        if (createReqVO.getStatus() != null && createReqVO.getStatus() == 1) {
            regulation.setPublishTime(LocalDateTime.now());
        }
        regulationMapper.insert(regulation);
        // 返回
        return regulation.getId();
    }

    @Override
    public void updateRegulation(RegulationSaveReqVO updateReqVO) {
        // 校验存在
        validateRegulationExists(updateReqVO.getId());
        // 更新
        RegulationDO updateObj = BeanUtils.toBean(updateReqVO, RegulationDO.class);
        // 如果状态是已发布，设置发布时间
        if (updateReqVO.getStatus() != null && updateReqVO.getStatus() == 1) {
            updateObj.setPublishTime(LocalDateTime.now());
        }
        regulationMapper.updateById(updateObj);
    }

    @Override
    public void deleteRegulation(Long id) {
        // 校验存在
        validateRegulationExists(id);
        // 删除
        regulationMapper.deleteById(id);
    }

    @Override
    public RegulationDO getRegulation(Long id) {
        return regulationMapper.selectById(id);
    }

    @Override
    public PageResult<RegulationDO> getRegulationPage(RegulationPageReqVO pageReqVO) {
        return regulationMapper.selectPage(pageReqVO);
    }

    @Override
    public void publishRegulation(Long id) {
        // 校验存在
        validateRegulationExists(id);
        // 更新状态为已发布
        RegulationDO updateObj = new RegulationDO();
        updateObj.setId(id);
        updateObj.setStatus(1);
        updateObj.setPublishTime(LocalDateTime.now());
        regulationMapper.updateById(updateObj);
    }

    @Override
    public void unpublishRegulation(Long id) {
        // 校验存在
        validateRegulationExists(id);
        // 更新状态为已下架
        RegulationDO updateObj = new RegulationDO();
        updateObj.setId(id);
        updateObj.setStatus(2);
        regulationMapper.updateById(updateObj);
    }

    @Override
    public byte[] downloadRegulation(Long id) throws Exception {
        // 校验存在
        RegulationDO regulation = validateRegulationExists(id);
        
        // 获取文件URL
        String fileUrl = regulation.getFileUrl();
        if (StrUtil.isBlank(fileUrl)) {
            throw new IllegalArgumentException("文件URL不能为空");
        }
        
        // 通过HTTP请求获取文件内容
        try (InputStream inputStream = HttpUtil.createGet(fileUrl).execute().bodyStream()) {
            return IoUtil.readBytes(inputStream);
        }
    }

    private RegulationDO validateRegulationExists(Long id) {
        RegulationDO regulation = regulationMapper.selectById(id);
        if (regulation == null) {
            throw exception(REGULATION_NOT_FOUND);
        }
        return regulation;
    }

} 