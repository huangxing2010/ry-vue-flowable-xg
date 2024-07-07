import request from '@/utils/request'

// 查询进出账管理列表
export function listFinance(query) {
  return request({
    url: '/oa/finance/list',
    method: 'get',
    params: query
  })
}

// 查询进项目出账管理列表
export function listFinancePro(query) {
  return request({
    url: '/oa/finance/listPro',
    method: 'get',
    params: query
  })
}

// 查询进出账管理详细
export function getFinance(financeId) {
  return request({
    url: '/oa/finance/' + financeId,
    method: 'get'
  })
}

export function getFinancePro(financeId) {
  return request({
    url: '/oa/finance/financePro' + financeId,
    method: 'get'
  })
}


// 根据时间范围查询列表
export function findFinance(params) {
  return request({
    url: '/oa/finance/getFinanceByTime',
    method: 'get',
    params:params
  })
}

// 项目资金管理，根据时间范围查询列表
export function findProjectFinance(query) {
  return request({
    url: '/oa/finance/findFinanceByTimeAndProjectId',
    method: 'get',
    params:query
  })
}

// 最近6个月核算列表
export function findFinanceList() {
  return request({
    url: '/oa/finance/findFinanceByMonth/',
    method: 'post'
  })
}

// 新增进出账管理
export function addFinance(data) {
  return request({
    url: '/oa/finance',
    method: 'post',
    data: data
  })
}

// 修改进出账管理
export function updateFinance(data) {
  return request({
    url: '/oa/finance',
    method: 'put',
    data: data
  })
}

// 删除进出账管理
export function delFinance(financeId) {
  return request({
    url: '/oa/finance/' + financeId,
    method: 'delete'
  })
}
