package com.cjxy.las.dao.base;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BaseDao {
	@PersistenceContext
	private EntityManager entityManager;

	public Session getSession() {
		Session session = null;
		if (entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
			throw new NullPointerException();
		} else {
			return session;
		}
	}
	public <R> List<R> find(String hql, Class<R> resultClass) throws Exception {
		return this.find(hql, resultClass);
	}

	public <R> List<R> find(String hql, Class<R> resultClass, Object value) throws Exception {
		return this.find(hql, resultClass, value);
	}

	/**
	 *
	 * @param <R>
	 * @param hql
	 * @param resultClass
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public <R> List<R> find(String hql, Class<R> resultClass, Object... values) throws Exception {
		Query<R> queryObject = this.getSession().createQuery(hql, resultClass);
		if (values != null) {
			for (int i = 1; i <= values.length; i++) {
				if (values[i - 1].getClass().isArray()) {
					Object[] pas = (Object[]) values[i - 1];
					queryObject.setParameterList(i, pas);
				} else {
					queryObject.setParameter(i, values[i - 1]);
				}
			}
		}
		return queryObject.list();
	}

	/**
	 * 分页查询
	 *
	 * @param <T>
	 * @param hql
	 * @param resultClass
	 * @param params
	 * @param pageNo
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findPage(String hql, Class<T> resultClass, Object[] params, int pageNo, int maxResults)
			throws Exception {
		Query<T> queryObject = this.getSession().createQuery(hql, resultClass);
		if (pageNo < 1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult = (pageNo - 1) * maxResults;
		if (params != null) {
			for (int i = 1; i <= params.length; i++) {
				if (params[i - 1].getClass().isArray()) {
					Object[] pas = (Object[]) params[i - 1];
					queryObject.setParameterList(i, pas);
				} else {
					queryObject.setParameter(i, params[i - 1]);
				}
			}
		}
		queryObject.setFirstResult(firstResult);
		queryObject.setMaxResults(maxResults);
		return queryObject.list();
	}

	public <T> List<T> findPage(String hql, Class<T> resultClass, int pageNo, int maxResults) throws Exception {
		Object[] params = null;
		return findPage(hql, resultClass, params, pageNo, maxResults);
	}

	public <T> List<T> findPage(String hql, Class<T> resultClass, Object param, int pageNo, int maxResults)
			throws Exception {
		return findPage(hql, resultClass, new Object[] { param }, pageNo, maxResults);
	}

	/**
	 *
	 * @param <R>
	 * @param hql
	 * @param resultClass
	 * @param params，hql:where field=:fld ,params:params.put("fld",fldvalue)
	 * @return
	 * @throws Exception
	 */
	public <R> List<R> find(String hql, Class<R> resultClass, Map<String, Object> params) throws Exception {
		Query<R> queryObject = this.getSession().createQuery(hql, resultClass);
		if (null != params) {
			for (Map.Entry<String, Object> entry : params.entrySet())
				queryObject.setParameter(entry.getKey(), entry.getValue());
		}
		return queryObject.list();
	}

	public List<Map<String, Object>> findMap(String hql) throws Exception {
		return this.findMap(hql);
	}

	public List<Map<String, Object>> findMap(String hql, Object value) throws Exception {
		return this.findMap(hql, value);
	}

	/**
	 * 传入的Hql必须显示的声明字段别名
	 *
	 * @param hql
	 * @param values
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMap(String hql, Object... values) throws Exception {
		Query<Map<String, Object>> queryObject = getSession().createQuery(hql,
				(Class<Map<String, Object>>) (Class<?>) Map.class);
		if (values != null) {
			for (int i = 1; i <= values.length; i++) {
				if (values[i - 1].getClass().isArray()) {
					Object[] pas = (Object[]) values[i - 1];
					queryObject.setParameterList(i, pas);
				} else {
					queryObject.setParameter(i, values[i - 1]);
				}
			}
		}
		queryObject.unwrap(Query.class).setTupleTransformer((tuple, aliases) -> {
			Map<String, Object> map = new HashMap<>();
			if (aliases[0] == null) {
				throw new java.lang.RuntimeException("hql:" + hql + ",未发现有效的别名");
			}
			map.put(aliases[0], tuple[0]);
			return map;
		});
		return queryObject.list();
	}

	/**
	 *
	 * @param hql
	 * @param values
	 * @param pageNo
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findPageMap(String hql, Object[] values, int pageNo, int maxResults)
			throws Exception {
		if (pageNo < 1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult = (pageNo - 1) * maxResults;
		Query<Map<String, Object>> queryObject = getSession().createQuery(hql,
				(Class<Map<String, Object>>) (Class<?>) Map.class);
		if (values != null) {
			for (int i = 1; i <= values.length; i++) {
				if (values[i - 1].getClass().isArray()) {
					Object[] pas = (Object[]) values[i - 1];
					queryObject.setParameterList(i, pas);
				} else {
					queryObject.setParameter(i, values[i - 1]);
				}
			}
		}

		queryObject.setFirstResult(firstResult);
		queryObject.setMaxResults(maxResults);

		queryObject.unwrap(Query.class).setTupleTransformer((tuple, aliases) -> {
			Map<String, Object> map = new HashMap<>();
			if (aliases[0] == null) {
				throw new java.lang.RuntimeException("hql:" + hql + ",未发现有效的别名");
			}
			map.put(aliases[0], tuple[0]);
			return map;
		});
		return queryObject.list();
	}

	public List<Map<String, Object>> findPageMap(String hql, Object param, int pageNo, int maxResults)
			throws Exception {
		return findPageMap(hql, new Object[] { param }, pageNo, maxResults);
	}

	public List<Map<String, Object>> findPageMap(String hql, int pageNo, int maxResults) throws Exception {
		return findPageMap(hql, null, pageNo, maxResults);
	}



	/**
	 *
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Map<String, Object>> findSql(String sql, final Object[] params) throws Exception {
		NativeQuery<Map<String, Object>> queryObject = getSession().createNativeQuery(sql);

		if (params != null) {
			for (int i = 1; i <= params.length; i++) {
				if (params[i - 1].getClass().isArray()) {
					Object[] pas = (Object[]) params[i - 1];
					queryObject.setParameterList(i, pas);
				} else {
					queryObject.setParameter(i, params[i - 1]);
				}
			}
		}
		queryObject.unwrap(NativeQuery.class).setTupleTransformer((tuple, aliases) -> {
			Map<String, Object> map = new HashMap<>();
			for(int i=0;i<tuple.length;i++) {
				if (aliases[i] == null) {
					throw new java.lang.RuntimeException("hql:" + sql + ",未发现有效的别名");
				}
				map.put(aliases[i], tuple[i]);
			}
			return map;
		});
		return queryObject.list();

	}

	public List<Map<String, Object>> findSql(String sql, final Object param) throws Exception {
		return this.findSql(sql,new Object[] {param});
	}

	public List<Map<String, Object>> findSql(String sql) throws Exception {
		return this.findSql(sql,null);
	}

	/**
	 *
	 * @param sql
	 * @param params
	 * @param pageNo
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Map<String, Object>> findSqlPage(String sql, final Object[] params, int pageNo, int maxResults) throws Exception {
		if (pageNo < 1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult = (pageNo - 1) * maxResults;
//		jakarta.persistence.Query queryObject=entityManager.createNativeQuery(sql);
		
		NativeQuery<Map<String, Object>> queryObject = getSession().createNativeQuery(sql);
		if (params != null) {
			for (int i = 1; i <= params.length; i++) {
				if (params[i - 1].getClass().isArray()) {
					Object[] pas = (Object[]) params[i - 1];
					queryObject.setParameterList(i, pas);
				} else {
					queryObject.setParameter(i, params[i - 1]);
				}
			}
		}
		queryObject.setFirstResult(firstResult);
		queryObject.setMaxResults(maxResults);
		queryObject.unwrap(NativeQuery.class).setTupleTransformer((tuple, aliases) -> {
			Map<String, Object> map = new HashMap<>();
			
			for(int i=0;i<tuple.length;i++) {
				if (aliases[i] == null) {
					throw new java.lang.RuntimeException("hql:" + sql + ",未发现有效的别名");
				}
				map.put(aliases[i], tuple[i]);
			}
			
			
			return map;
		});
		return queryObject.list();
	}

	public List<Map<String, Object>> findSqlPage(String sql, int pageNo, int maxResults) throws Exception {
		return findSqlPage(sql,null,pageNo,  maxResults);
	}


	/**
	 * 仅支持单个实体bean 查询
	 */
	public <T> Map<String, Object> beanToHql(T t) {
		Map<String, Object> map = new HashMap<>();
		try {
			StringBuilder hql = new StringBuilder("from " + t.getClass().getName() + " o  ");
			List<Object> param = new ArrayList<>();
			if (null != t) {
				List<Field> fields = getFiledsInfo(t);
				int index = 0;
				for (int i = 0; i < fields.size(); i++) {
					Field field = fields.get(i);
					field.setAccessible(true);// 设置可访问私有
					if (field.getType().getName().equals("java.lang.String")) {
						if (null != field.get(t) && field.get(t).toString().length() > 0
								&& !field.get(t).toString().equals("")) {
							hql.append((index == 0 ? " where " : " and ") + "  o." + field.getName() + " like ?"
									+ (++index));
							param.add('%' + field.get(t).toString() + '%');
						}
					} else {
						if (null != field.get(t)) {
							hql.append(
									(index == 0 ? " where " : " and ") + "  o." + field.getName() + " =?" + (++index));
							param.add(field.get(t));
						}
					}
				}
			}
			log.debug(hql.toString());
			map.put("hql", hql.toString());
			map.put("param", param);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 单个bean的简单查询
	 * @param <R>
	 * @param t
	 * @param pageNo
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <R> List<R> queryBean(R t, int pageNo, int maxResults) throws Exception {
		Class<R> clzzClass=(Class<R>) t.getClass();
		Map<String, Object> map = beanToHql(t);
		String hql = map.get("hql").toString();
		List<Object> param = (List<Object>) map.get("param");
		log.debug(hql);
		if (maxResults == 0) {
			return  this.find(hql,clzzClass, param.toArray());
		}
		return this.findPage(hql.toString(),clzzClass, param.toArray(), pageNo, maxResults);
	}

	public <R> List<R> queryBean(R t) throws Exception {
		return this.queryBean(t,0, 0);
	}

	/**
	 * 根据hql 自动生成统计类语句，获取记录数
	 * @param hql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryBeanCountByHql(String hql, Object[] params) throws Exception {
		hql = hql.trim();
		if (!hql.substring(0, 4).equalsIgnoreCase("from")) {
			log.error("传入的HQL无法合成统计语句，正确 形式如下  from beanName o where ...");
			throw new DataAccessResourceFailureException("传入的HQL无法合成统计语句，正确 形式如下  from beanName o where ...");
		}
		String newhql = "select count(*) " + hql;
		List<Object> ls = this.find(newhql,Object.class, params);
		int count = Integer.valueOf(ls.get(0).toString());
		return count;
	}
	/**
	 * 获取单个bean的记录数
	 * @param <T>
	 * @param t
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> int queryBeanCount(T t) throws Exception {
		Map<String, Object> map = beanToHql(t);
		String hql = map.get("hql").toString();
		List<Object> param = (List<Object>) map.get("param");
		log.debug(hql.toString());
		return queryBeanCountByHql(hql.toString(), param.toArray());
	}



	public <T> Page<T> findJpaPage(final String hql, Class<T> resultClass,final Object[] params, int pageNo, int maxResults) throws Exception {
		if (pageNo < 1)
			throw new Exception("pageNo 必须大于 0");
		
		List<T> ls=this.findPage(hql, resultClass, params,pageNo,maxResults);
		//拼装 获取数量的HQL语句
		String newHql= hql.toLowerCase();//转成小写
		int fromIndex=newHql.indexOf("from ");//搜索第一个from
		if(fromIndex==-1)
			throw new Exception("未能找到关键字'from '");
		newHql=hql.substring(fromIndex); //截取子串

		long total=this.queryBeanCountByHql(newHql, params);
		//使用JPA分页器
		PageRequest pageRequest = PageRequest.of((pageNo-1), maxResults);
		//构造分页结构
		Page<T> page=new PageImpl<>(ls, pageRequest, total);
		return page;
	}




	private <T> String getClassContext(T t) {
		StringBuffer sb = new StringBuffer("操作对象：" + t.getClass().getName() + "操作内容：");
		try {
			if (null != t) {
				Field[] fields = t.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);// 设置可访问私有
					sb.append(field.getName() + "=" + field.get(t) + " ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 利用Java反射根据类的名称获取属性信息和父类的属性信息(目前仅支持一层父类)
	 *
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */

	private <T> List<Field> getFiledsInfo(T t) throws ClassNotFoundException {
		List<Field> list = new ArrayList<>();
		Class<?> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		list.addAll(Arrays.asList(fields));
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null) {
			Field[] superFields = superClazz.getDeclaredFields();
			list.addAll(Arrays.asList(superFields));
		}
		return list;
	}

}

