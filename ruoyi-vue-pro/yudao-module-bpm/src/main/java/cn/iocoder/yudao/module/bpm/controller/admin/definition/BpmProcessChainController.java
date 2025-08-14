package cn.iocoder.yudao.module.bpm.controller.admin.definition;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain.*;
import java.util.Map;
import cn.iocoder.yudao.module.bpm.convert.definition.BpmProcessChainConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessChainDO;
import cn.iocoder.yudao.module.bpm.service.definition.BpmProcessChainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import java.util.List;

@Tag(name = "管理后台 - 串联流程关系")
@RestController
@RequestMapping("/bpm/process-chain")
@Validated
public class BpmProcessChainController {

    @Resource
    private BpmProcessChainService processChainService;

    @PostMapping("/create")
    @Operation(summary = "创建串联流程关系")
    @PreAuthorize("@ss.hasPermission('bpm:process-chain:create')")
    public CommonResult<Long> createProcessChain(@Valid @RequestBody BpmProcessChainCreateReqVO createReqVO) {
        return success(processChainService.createProcessChain(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新串联流程关系")
    @PreAuthorize("@ss.hasPermission('bpm:process-chain:update')")
    public CommonResult<Boolean> updateProcessChain(@Valid @RequestBody BpmProcessChainUpdateReqVO updateReqVO) {
        processChainService.updateProcessChain(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除串联流程关系")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:process-chain:delete')")
    public CommonResult<Boolean> deleteProcessChain(@RequestParam("id") Long id) {
        processChainService.deleteProcessChain(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得串联流程关系")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<BpmProcessChainRespVO> getProcessChain(@RequestParam("id") Long id) {
        BpmProcessChainDO processChain = processChainService.getProcessChain(id);
        return success(BpmProcessChainConvert.INSTANCE.convert(processChain));
    }

    @GetMapping("/page")
    @Operation(summary = "获得串联流程关系分页")
    public CommonResult<PageResult<BpmProcessChainRespVO>> getProcessChainPage(@Valid BpmProcessChainPageReqVO pageVO) {
        PageResult<BpmProcessChainDO> pageResult = processChainService.getProcessChainPage(pageVO);
        return success(BpmProcessChainConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/get-by-source")
    @Operation(summary = "根据源流程标识查询可发起的后续流程")
    @Parameter(name = "sourceProcessKey", description = "源流程标识", required = true, example = "cg_wzsq")
    public CommonResult<List<BpmProcessChainRespVO>> getProcessChainsBySourceKey(@RequestParam("sourceProcessKey") String sourceProcessKey) {
        List<BpmProcessChainDO> processChains = processChainService.getProcessChainsBySourceKey(sourceProcessKey);
        return success(BpmProcessChainConvert.INSTANCE.convertList(processChains));
    }

    @GetMapping("/get-by-target")
    @Operation(summary = "根据目标流程标识查询前置流程")
    @Parameter(name = "targetProcessKey", description = "目标流程标识", required = true, example = "cg_zcrk")
    public CommonResult<List<BpmProcessChainRespVO>> getProcessChainsByTargetKey(@RequestParam("targetProcessKey") String targetProcessKey) {
        List<BpmProcessChainDO> processChains = processChainService.getProcessChainsByTargetKey(targetProcessKey);
        return success(BpmProcessChainConvert.INSTANCE.convertList(processChains));
    }

    @PostMapping("/generate-variables")
    @Operation(summary = "生成目标流程变量")
    public CommonResult<Map<String, Object>> generateTargetProcessVariables(@RequestBody BpmProcessChainGenerateVariablesReqVO reqVO) {
        Map<String, Object> targetVariables = processChainService.generateTargetProcessVariables(
                reqVO.getSourceProcessKey(), reqVO.getTargetProcessKey(), reqVO.getSourceVariables());
        return success(targetVariables);
    }

} 