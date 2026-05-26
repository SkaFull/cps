import request from '@/utils/request'

// 查询联盟信息列表
export function listUnionPlatform(query) {
  return request({
    url: '/system/unionPlatform/list',
    method: 'get',
    params: query
  })
}

// 查询联盟信息详细
export function getUnionPlatform(id) {
  return request({
    url: '/system/unionPlatform/' + id,
    method: 'get'
  })
}

// 根据联盟类型查询可用的联盟信息
export function getAvailableByType(platformType) {
  return request({
    url: '/system/unionPlatform/available/' + platformType,
    method: 'get'
  })
}

// 新增联盟信息
export function addUnionPlatform(data) {
  return request({
    url: '/system/unionPlatform',
    method: 'post',
    data: data
  })
}

// 修改联盟信息
export function updateUnionPlatform(data) {
  return request({
    url: '/system/unionPlatform',
    method: 'put',
    data: data
  })
}

// 删除联盟信息
export function delUnionPlatform(id) {
  return request({
    url: '/system/unionPlatform/' + id,
    method: 'delete'
  })
}

// 导出联盟信息
export function exportUnionPlatform(query) {
  return request({
    url: '/system/unionPlatform/export',
    method: 'post',
    params: query
  })
}

// 分配联盟信息给代理申请
export function assignUnionPlatforms(applyId, unionPlatformIds) {
  return request({
    url: '/system/tbkApply/assign/' + applyId,
    method: 'post',
    data: unionPlatformIds
  })
}

// 取消代理申请的联盟分配
export function cancelAssignment(applyId) {
  return request({
    url: '/system/tbkApply/cancel/' + applyId,
    method: 'delete'
  })
}

// 查询代理申请已分配的联盟信息
export function getAssignedUnions(applyId) {
  return request({
    url: '/system/tbkApply/assigned/' + applyId,
    method: 'get'
  })
}

// 根据申请ID查询可分配的联盟信息（按申请类型过滤，仅返回未被引用的）
export function getAvailableUnions(applyId) {
  return request({
    url: '/system/tbkApply/available/' + applyId,
    method: 'get'
  })
}

// 获取当前用户绑定的联盟平台列表
export function getUserBoundPlatforms() {
  return request({
    url: '/system/unionPlatform/userBound',
    method: 'get'
  })
}

// PDD授权备案
export function pddAuthRecord(relationId) {
  return request({
    url: '/system/tbkApply/pddAuth/' + relationId,
    method: 'post'
  })
}
