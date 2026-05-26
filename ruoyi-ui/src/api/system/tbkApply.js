import request from '@/utils/request'

// 查询淘宝客代理申请列表
export function listTbkApply(query) {
  return request({
    url: '/system/tbkApply/list',
    method: 'get',
    params: query
  })
}

// 查询淘宝客代理申请详细
export function getTbkApply(id) {
  return request({
    url: '/system/tbkApply/' + id,
    method: 'get'
  })
}

// 审核代理申请
export function auditTbkApply(id, status, remark) {
  return request({
    url: '/system/tbkApply/audit/' + id,
    method: 'put',
    params: {
      status: status,
      remark: remark
    }
  })
}

// 修改代理申请状态
export function changeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/tbkApply/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除淘宝客代理申请
export function delTbkApply(id) {
  return request({
    url: '/system/tbkApply/' + id,
    method: 'delete'
  })
}
