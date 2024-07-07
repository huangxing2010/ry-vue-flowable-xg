import request from '@/utils/request'

// 查询采购意向列表
export function listPurpose(query) {
  return request({
    url: '/oa/purpose/list',
    method: 'get',
    params: query
  })
}

// 查询采购意向详细
export function getPurpose(purposeId) {
  return request({
    url: '/oa/purpose/' + purposeId,
    method: 'get'
  })
}

// 新增采购意向
export function addPurpose(data) {
  return request({
    url: '/oa/purpose',
    method: 'post',
    data: data
  })
}

// 修改采购意向
export function updatePurpose(data) {
  return request({
    url: '/oa/purpose',
    method: 'put',
    data: data
  })
}

// 删除采购意向
export function delPurpose(purposeId) {
  return request({
    url: '/oa/purpose/' + purposeId,
    method: 'delete'
  })
}
