package cn.iocoder.yudao.module.system.controller.admin.regulation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.framework.file.core.utils.FileTypeUtils;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationRespVO;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.regulation.RegulationDO;
import cn.iocoder.yudao.module.system.service.regulation.RegulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import cn.hutool.core.util.StrUtil;

@Tag(name = "管理后台 - 规章制度")
@RestController
@RequestMapping("/system/regulation")
@Validated
public class RegulationController {

    @Resource
    private RegulationService regulationService;

    @PostMapping("/create")
    @Operation(summary = "创建规章制度")
    @PreAuthorize("@ss.hasPermission('system:regulation:create')")
    public CommonResult<Long> createRegulation(@Valid @RequestBody RegulationSaveReqVO createReqVO) {
        return success(regulationService.createRegulation(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改规章制度")
    @PreAuthorize("@ss.hasPermission('system:regulation:update')")
    public CommonResult<Boolean> updateRegulation(@Valid @RequestBody RegulationSaveReqVO updateReqVO) {
        regulationService.updateRegulation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除规章制度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:regulation:delete')")
    public CommonResult<Boolean> deleteRegulation(@RequestParam("id") Long id) {
        regulationService.deleteRegulation(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得规章制度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<RegulationRespVO> getRegulation(@RequestParam("id") Long id) {
        RegulationDO regulation = regulationService.getRegulation(id);
        return success(BeanUtils.toBean(regulation, RegulationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得规章制度分页")
    public CommonResult<PageResult<RegulationRespVO>> getRegulationPage(@Valid RegulationPageReqVO pageReqVO) {
        PageResult<RegulationDO> pageResult = regulationService.getRegulationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RegulationRespVO.class));
    }

    @PostMapping("/publish")
    @Operation(summary = "发布规章制度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:regulation:update')")
    public CommonResult<Boolean> publishRegulation(@RequestParam("id") Long id) {
        regulationService.publishRegulation(id);
        return success(true);
    }

    @PostMapping("/unpublish")
    @Operation(summary = "下架规章制度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:regulation:update')")
    public CommonResult<Boolean> unpublishRegulation(@RequestParam("id") Long id) {
        regulationService.unpublishRegulation(id);
        return success(true);
    }

    @GetMapping("/download")
    @Operation(summary = "下载规章制度文件")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:regulation:query')")
    public void downloadRegulation(@RequestParam("id") Long id, HttpServletResponse response) throws Exception {
        // 获取规章制度信息
        RegulationDO regulation = regulationService.getRegulation(id);
        if (regulation == null) {
            throw new IllegalArgumentException("规章制度不存在");
        }
        
        // 获取文件内容
        byte[] content = regulationService.downloadRegulation(id);
        
        // 构建文件名：使用规章制度标题 + 原文件扩展名
        String fileName = regulation.getTitle();
        if (StrUtil.isNotBlank(regulation.getFileName())) {
            String extension = "";
            int lastDotIndex = regulation.getFileName().lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = regulation.getFileName().substring(lastDotIndex);
            }
            fileName += extension;
        }
        
        // 输出文件
        FileTypeUtils.writeAttachment(response, fileName, content);
    }

} 