import request from '@/utils/request'

// 查询合同管理列表
export function listOaContract(query) {
  return request({
    url: '/oa/oaContract/list',
    method: 'get',
    params: query
  })
}

// 查询合同管理详细
export function getOaContract(contractId) {
  return request({
    url: '/oa/oaContract/' + contractId,
    method: 'get'
  })
}

// 根据项目id查询合同信息
export function findContractByProjectId(query) {
  return request({
    url: '/oa/oaContract/findContract',
    method: 'get',
    params: query
  })
}

// 根据项目id查询合同列表
export function selectContractByProjectId(query) {
  return request({
    url: '/oa/oaContract/selectContract',
    method: 'get',
    params: query
  })
}

// 新增合同管理
export function addOaContract(data) {
  return request({
    url: '/oa/oaContract',
    method: 'post',
    data: data
  })
}

// 修改合同管理
export function updateOaContract(data) {
  return request({
    url: '/oa/oaContract',
    method: 'put',
    data: data
  })
}

// 删除合同管理
export function delOaContract(contractId) {
  return request({
    url: '/oa/oaContract/' + contractId,
    method: 'delete'
  })
}
