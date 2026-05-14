package net.hwyz.iov.cloud.edd.org.service.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.OrgMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.OrgPo;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.framework.web.domain.TreeSelect;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织结构应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgAppService {

    private final OrgMapper orgMapper;

    /**
     * 查询组织结构
     *
     * @param code       组织架构代码
     * @param name       组织架构名称
     * @param orgType    组织架构类型
     * @param parentCode 上级组织代码
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @return 车辆平台列表
     */
    public List<OrgPo> search(String code, String name, String orgType, String parentCode, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("name", ParamHelper.fuzzyQueryParam(name));
        if (StrUtil.isNotBlank(parentCode)) {
            OrgPo parentOrg = getOrgByCode(parentCode);
            if (parentOrg != null) {
                map.put("parentId", parentOrg.getId());
            }
        }
        map.put("orgType", orgType);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return orgMapper.selectPoByMap(map);
    }

    /**
     * 查询组织树结构信息
     *
     * @param code       组织结构代码
     * @param name       组织结构名称
     * @param orgType    组织架构类型
     * @param parentCode 上级代码
     * @return 组织树结构信息
     */
    public List<TreeSelect> selectOrgTreeList(String code, String name, String orgType, String parentCode) {
        List<OrgPo> orgPoList = search(code, name, orgType, parentCode, null, null);
        return buildDeptTreeSelect(orgPoList);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param orgPoList 组织列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptTreeSelect(List<OrgPo> orgPoList) {
        List<OrgPo> orgTrees = buildOrgTree(orgPoList);
        return orgTrees.stream()
                .map(this::buildDeptTreeSelect)
                .collect(Collectors.toList());
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param org 组织结构信息
     * @return 下拉树结构
     */
    private TreeSelect buildDeptTreeSelect(OrgPo org) {
        TreeSelect treeSelect = new TreeSelect();
        treeSelect.setId(org.getCode());
        treeSelect.setLabel(org.getName());
        treeSelect.setType(org.getOrgType());
        treeSelect.setChildren(org.getChildren().stream().map(this::buildDeptTreeSelect).collect(Collectors.toList()));
        return treeSelect;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param orgPoList 组织列表
     * @return 树结构列表
     */
    private List<OrgPo> buildOrgTree(List<OrgPo> orgPoList) {
        List<OrgPo> returnList = new ArrayList<>();
        List<Long> tempList = orgPoList.stream().map(OrgPo::getId).collect(Collectors.toList());
        for (OrgPo org : orgPoList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(org.getParentId())) {
                recursionFn(orgPoList, org);
                returnList.add(org);
            }
        }
        if (returnList.isEmpty()) {
            returnList = orgPoList;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<OrgPo> list, OrgPo t) {
        // 得到子节点列表
        List<OrgPo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (OrgPo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<OrgPo> getChildList(List<OrgPo> list, OrgPo t) {
        List<OrgPo> tlist = new ArrayList<>();
        Iterator<OrgPo> it = list.iterator();
        while (it.hasNext()) {
            OrgPo n = it.next();
            if (ObjUtil.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<OrgPo> list, OrgPo t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 检查用户ID是否唯一
     *
     * @param orgId 组织结构ID
     * @param code  组织结构代码
     * @return 结果
     */
    public Boolean checkCodeUnique(Long orgId, String code) {
        if (ObjUtil.isNull(orgId)) {
            orgId = -1L;
        }
        OrgPo orgPo = getOrgByCode(code);
        return !ObjUtil.isNotNull(orgPo) || orgPo.getId().longValue() == orgId.longValue();
    }

    /**
     * 根据组织结构ID获取组织结构
     *
     * @param id 主键ID
     * @return 组织结构
     */
    public OrgPo getOrgById(Long id) {
        return orgMapper.selectPoById(id);
    }

    /**
     * 根据组织结构代码获取组织结构
     *
     * @param code 组织结构代码
     * @return 组织结构
     */
    public OrgPo getOrgByCode(String code) {
        return orgMapper.selectPoByCode(code);
    }

    /**
     * 新增组织结构
     *
     * @param org 组织结构
     * @return 结果
     */
    public int createOrg(OrgPo org) {
        if (org.getParentId() > 0) {
            OrgPo parentOrg = getOrgById(org.getParentId());
            org.setAncestors(parentOrg.getAncestors() + "," + org.getParentId());
        }
        return orgMapper.insertPo(org);
    }

    /**
     * 修改组织结构
     *
     * @param org 组织结构
     * @return 结果
     */
    public int modifyOrg(OrgPo org) {
        if (org.getParentId() > 0) {
            OrgPo parentOrg = getOrgById(org.getParentId());
            org.setAncestors(parentOrg.getAncestors() + "," + org.getParentId());
        }
        return orgMapper.updatePo(org);
    }

    /**
     * 批量删除组织结构
     *
     * @param ids 组织结构ID数组
     * @return 结果
     */
    public int deleteOrgByIds(Long[] ids) {
        return orgMapper.batchPhysicalDeletePo(ids);
    }

}
