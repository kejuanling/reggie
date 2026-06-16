/**
 * 地址管理API（移动端）
 * 对应后端 AddressBookController
 */

/**
 * 获取当前用户所有地址列表
 * @returns {Promise} 地址列表
 */
function addressListApi() {
  return $axios({
    url: '/addressBook/list',
    method: 'get'
  })
}

/**
 * 获取当前用户默认地址
 * @returns {Promise} 默认地址
 */
function getDefaultAddressApi() {
  return $axios({
    url: '/addressBook/default',
    method: 'get'
  })
}

/**
 * 新增地址
 * @param {Object} data - 地址信息
 * @returns {Promise} 操作结果
 */
function addAddressApi(data) {
  return $axios({
    url: '/addressBook',
    method: 'post',
    data
  })
}

/**
 * 修改地址
 * @param {Object} data - 地址信息
 * @returns {Promise} 操作结果
 */
function updateAddressApi(data) {
  return $axios({
    url: '/addressBook',
    method: 'put',
    data
  })
}

/**
 * 删除地址
 * @param {Object} params - 参数
 * @param {number} params.id - 地址ID
 * @returns {Promise} 操作结果
 */
function deleteAddressApi(params) {
  return $axios({
    url: '/addressBook',
    method: 'delete',
    params: { id: params.id || params.ids }
  })
}

/**
 * 根据ID查询单个地址
 * @param {number} id - 地址ID
 * @returns {Promise} 地址详情
 */
function addressFindOneApi(id) {
  return $axios({
    url: `/addressBook?id=${id}`,
    method: 'get'
  })
}

/**
 * 设置默认地址
 * @param {Object} data - 地址信息（包含id）
 * @returns {Promise} 操作结果
 */
function setDefaultAddressApi(data) {
  return $axios({
    url: '/addressBook/default',
    method: 'put',
    data
  })
}