import request from '@/utils/request'

// 查询佣金配置列表
export function listCommissionConfig(query) {
  return request({
    url: '/system/commissionConfig/list',
    method: 'get',
    params: query
  })
}

// 查询佣金配置详细
export function getCommissionConfig(id) {
  return request({
    url: '/system/commissionConfig/' + id,
    method: 'get'
  })
}

// 新增佣金配置
export function addCommissionConfig(data) {
  return request({
    url: '/system/commissionConfig',
    method: 'post',
    data: data
  })
}

// 修改佣金配置
export function updateCommissionConfig(data) {
  return request({
    url: '/system/commissionConfig',
    method: 'put',
    data: data
  })
}

// 删除佣金配置
export function delCommissionConfig(id) {
  return request({
    url: '/system/commissionConfig/' + id,
    method: 'delete'
  })
}
