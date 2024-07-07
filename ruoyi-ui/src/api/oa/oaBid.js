import request from '@/utils/request'

// 查询投标管理列表
export function listOaBid(query) {
  return request({
    url: '/oa/oaBid/list',
    method: 'get',
    params: query
  })
}

// 查询投标管理详细
export function getOaBid(bidId) {
  return request({
    url: '/oa/oaBid/' + bidId,
    method: 'get'
  })
}

// 新增投标管理
export function addOaBid(data) {
  return request({
    url: '/oa/oaBid',
    method: 'post',
    data: data
  })
}

// 修改投标管理
export function updateOaBid(data) {
  return request({
    url: '/oa/oaBid',
    method: 'put',
    data: data
  })
}

// 删除投标管理
export function delOaBid(bidId) {
  return request({
    url: '/oa/oaBid/' + bidId,
    method: 'delete'
  })
}
