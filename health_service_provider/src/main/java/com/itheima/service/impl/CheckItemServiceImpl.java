package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author pc
 * 项目实现类
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添加
     *
     * @param checkItem 对象
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示的条数
     * @param queryString 查询条件
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);

        //调用dao层的selectByCondition方法查询
        Page<CheckItem> checkItems = checkItemDao.selectByCondition(queryString);

        long total = checkItems.getTotal();
        List<CheckItem> result = checkItems.getResult();

        return new PageResult(total, result);
    }

    /**
     * 根据id删除检查项
     *
     * @param id 用户id
     */
    @Override
    public void delete(Integer id) throws RuntimeException {
        //先判断检查项是否根检查组有关联
        Long count = checkItemDao.findCountByCheckItemId(id);
        //有，不能删除
        if(count > 0){
            throw  new RuntimeException("根检查组有关联，不能删除！");
        }
        //没有就调用删除方法
        checkItemDao.deleteById(id);
    }

    /**
     * 编辑
     * @param checkItem 对象
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 根据id查询
     * @param id
     */
    @Override
    public CheckItem findById(Integer id) {
      return checkItemDao.findById(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
