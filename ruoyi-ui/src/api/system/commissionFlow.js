import request from '@/utils/request'

// 查询佣金流水列表
export function listFlow(query) {
  return request({
    url: '/system/commission/flow/list',
    method: 'get',
    params: query
  })
}

// 查询佣金流水详细
export function getFlow(flowId) {
  return request({
    url: '/system/commission/flow/' + flowId,
    method: 'get'
  })
}

// 导出佣金流水
export function exportFlow(query) {
  return request({
    url: '/system/commission/flow/export',
    method: 'get',
    params: query
  })
}
