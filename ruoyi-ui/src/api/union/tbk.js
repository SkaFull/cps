import request from '@/utils/request'

// 店铺搜索
export function getShop(query) {
  return request({
    url: '/union/tbk/getShop',
    method: 'get',
    params: query
  })
}

// 权益物料精选
export function dgOptimusPromotion(query) {
  return request({
    url: '/union/tbk/dgOptimusPromotion',
    method: 'get',
    params: query
  })
}

// 物料id列表查询
export function optimusTouMaterialIdsGet(query) {
  return request({
    url: '/union/tbk/optimusTouMaterialIdsGet',
    method: 'get',
    params: query
  })
}

// 物料精选升级版
export function dgMaterialRecommend(query) {
  return request({
    url: '/union/tbk/dgMaterialRecommend',
    method: 'get',
    params: query
  })
}

// 物料搜索升级版
export function dgMaterialOptionalUpgrade(query) {
  return request({
    url: '/union/tbk/dgMaterialOptionalUpgrade',
    method: 'get',
    params: query
  })
}

// 获取淘口令
export function getTpwd(data) {
  return request({
    url: '/union/tbk/getTpwd',
    method: 'get',
    params: data
  })
}

// 获取短连接
export function getShortUrl(data) {
  return request({
    url: '/union/tbk/getShortUrl',
    method: 'get',
    params: data
  })
}
