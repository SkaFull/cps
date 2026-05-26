import request from '@/utils/request'

// 查询订单佣金列表
export function listOrderCommission(query) {
  return request({
    url: '/system/orderCommission/list',
    method: 'get',
    params: query
  })
}

// 查询订单佣金详细
export function getOrderCommission(id) {
  return request({
    url: '/system/orderCommission/' + id,
    method: 'get'
  })
}

// 新增订单佣金
export function addOrderCommission(data) {
  return request({
    url: '/system/orderCommission',
    method: 'post',
    data: data
  })
}

// 修改订单佣金
export function updateOrderCommission(data) {
  return request({
    url: '/system/orderCommission',
    method: 'put',
    data: data
  })
}

// 删除订单佣金
export function delOrderCommission(id) {
  return request({
    url: '/system/orderCommission/' + id,
    method: 'delete'
  })
}

// 订单结算
export function settleOrder(id, data) {
  return request({
    url: '/system/orderCommission/settle/' + id,
    method: 'post',
    data: data
  })
}

// 订单退款
export function refundOrder(id) {
  return request({
    url: '/system/orderCommission/refund/' + id,
    method: 'post'
  })
}
