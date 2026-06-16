/**
 * 订单管理API
 * 对应后端 OrderController 和 OrderDetailController
 */

/**
 * 订单分页查询（管理端）
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @param {string} [params.number] - 订单号（模糊搜索）
 * @returns {Promise} 分页结果
 */
const getOrderDetailPage = (params) => {
  return $axios({
    url: '/order/page',
    method: 'get',
    params
  })
}

/**
 * 查询订单明细列表
 * @param {number} id - 订单ID
 * @returns {Promise} 订单明细列表
 */
const queryOrderDetailById = (id) => {
  return $axios({
    url: `/orderDetail/${id}`,
    method: 'get'
  })
}

/**
 * 更新订单状态（取消、派送、完成）
 * @param {Object} params - 订单信息
 * @param {number} params.id - 订单ID
 * @param {number} params.status - 订单状态
 * @returns {Promise} 操作结果
 */
const editOrderDetail = (params) => {
  return $axios({
    url: '/order',
    method: 'put',
    data: { ...params }
  })
}
