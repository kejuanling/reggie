/**
 * 订单API（移动端）
 * 对应后端 OrderController
 */

/**
 * 提交订单
 * @param {Object} data - 订单信息
 * @returns {Promise} 操作结果
 */
function addOrderApi(data) {
  return $axios({
    url: '/order/submit',
    method: 'post',
    data
  })
}

/**
 * 用户订单分页查询
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @returns {Promise} 分页结果
 */
function orderPagingApi(params) {
  return $axios({
    url: '/order/userPage',
    method: 'get',
    params
  })
}

/**
 * 再来一单
 * @param {Object} data - 订单信息
 * @param {number} data.id - 订单ID
 * @returns {Promise} 操作结果
 */
function orderAgainApi(data) {
  return $axios({
    url: '/order/again',
    method: 'post',
    data
  })
}