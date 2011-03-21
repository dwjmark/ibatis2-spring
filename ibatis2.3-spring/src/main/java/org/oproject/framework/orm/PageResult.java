package org.oproject.framework.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分页结果对象
 * </p>
 * @see java.io.Serializable
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class PageResult<T> implements Serializable
{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2945092464731479394L;
	
	/**
	 * Null Object
	 */
	public static final PageResult<Object> EMPTY_PAGE = new PageResult<Object>(){

		/**
		 * 默认的序列化ID
		 */
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see org.oproject.framework.orm.PageResult#getResultList()
		 * 空对象，返回一个空List
		 */
		@Override
		public List<Object> getResultList()
		{
			return new ArrayList<Object>(0);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void setResultList(List resultList)
		{
			throw new UnsupportedOperationException("this is a null object.");
		}
	};

	/**
	 * 默认每页显示记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 最大页数
	 */
	public static final int MAX_PAGE_SIZE = 0;

	/**
	 * 页容量
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 开始行
	 */
	private int startRow = 0;

	/**
	 * 当前页的容量
	 */
	private int currentPageSize = 0;

	/**
	 * 全部记录数
	 */
	private int totalSize = 0;

	/**
	 * 当前页码
	 */
	private int currentPageNo = 1;

	/**
	 * 总页码
	 */
	private int totalPageCount = 0;

	/**
	 * 查询结果集合
	 */
	private List<T> resultList;

	/**
	 * 私有空构造函数
	 */
	private PageResult()
	{
	}

	/**
	 * 构造函数
	 * 
	 * @param totalSize
	 *            总记录数
	 * @param pageSize
	 *            当前页显示记录数
	 * @param currentPageNo
	 *            当前页数
	 */
	public PageResult(int totalSize, int pageSize, int currentPageNo)
	{
		if (totalSize < 0)
			this.setTotalSize(0);
		else
			this.setTotalSize(totalSize);
		if (pageSize < 1)
			this.setPageSize(DEFAULT_PAGE_SIZE);
		else
			this.setPageSize(pageSize);
		if (getTotalSize() % getPageSize() == 0)
			this.setTotalPageCount(getTotalSize() / getPageSize());
		else
			this.setTotalPageCount(getTotalSize() / getPageSize() + 1);
		if (currentPageNo > this.getTotalPageCount())
			this.setCurrentPageNo(this.getTotalPageCount());
		else
			this.setCurrentPageNo(currentPageNo);
		this.setStartRow(this.getPageSize() * (this.getCurrentPageNo() - 1));
		if (this.getCurrentPageNo() == this.getTotalPageCount())
		{
			this.setCurrentPageSize(this.getTotalSize() - this.getStartRow());
		}
		else if (this.getCurrentPageNo() < this.getTotalPageCount()
				&& this.getCurrentPageNo() >= 1)
		{
			this.setCurrentPageSize(this.getPageSize());
		}
		else
		{
			this.setCurrentPageSize(0);
		}
	}

	private void setCurrentPageNo(int currentPageNo)
	{
		if (currentPageNo < 1)
			currentPageNo = 1;
		this.currentPageNo = currentPageNo;
	}

	private void setCurrentPageSize(int currentPageSize)
	{
		this.currentPageSize = currentPageSize;
	}

	private void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public void setResultList(List<T> resultList)
	{
		this.resultList = resultList;
		if (resultList != null)
			this.setCurrentPageSize(resultList.size());
	}

	private void setStartRow(int startRow)
	{
		this.startRow = startRow;
	}

	private void setTotalPageCount(int totalPageCount)
	{
		this.totalPageCount = totalPageCount;
	}

	private void setTotalSize(int totalSize)
	{
		this.totalSize = totalSize;
	}

	/**
	 * 返回当前页码
	 * @return
	 */
	public int getCurrentPageNo()
	{
		return currentPageNo;
	}

	/**
	 * 返回当前也实际记录数
	 * @return
	 */
	public int getCurrentPageSize()
	{
		return currentPageSize;
	}

	/**
	 * 返回页记录数(如果是最后一页，请使用getCurrentPageSize()方法
	 * 该结果为设置的页容量大小。)
	 * @return
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * 返回查询结果集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getResultList()
	{
		return (List<E>)resultList;
	}

	/**
	 * 返回开始行数
	 * @return
	 */
	public int getStartRow()
	{
		return startRow;
	}

	/**
	 * 返回总页数
	 * @return
	 */
	public int getTotalPageCount()
	{
		return totalPageCount;
	}

	/**
	 * 返回总记录数
	 * @return
	 */
	public int getTotalSize()
	{
		return totalSize;
	}

	/**
	 * 下一页页码
	 * @return
	 */
	public int getNextPageNo()
	{
		if (getHasNextPage())
			return getCurrentPageNo() + 1;
		else
			return getCurrentPageNo();
	}

	/**
	 * 前一页页码
	 * @return
	 */
	public int getPreviousPageNo()
	{
		if (getHasPreviousPage())
			return getCurrentPageNo() - 1;
		else
			return getCurrentPageNo();
	}

	/**
	 * 是否还有前一页
	 * @return
	 */
	public boolean getHasPreviousPage()
	{
		if (this.getCurrentPageNo() > 1)
			return true;
		else
			return false;
	}

	/**
	 * 是否存在下一页
	 * @return
	 */
	public boolean getHasNextPage()
	{
		if (this.getCurrentPageNo() < this.getTotalPageCount())
			return true;
		else
			return false;
	}

	/**
	 * 是否是第一页
	 * @return
	 */
	public boolean getIsFirstPage()
	{
		// if(getTotalPageCount()>0 && getCurrentPageNo() == 1)
		if (getCurrentPageNo() < 2)
			return true;
		else
			return false;
	}

	/**
	 * 是否是最后一页
	 * @return
	 */
	public boolean getIsLastPage()
	{
		if (getTotalPageCount() <= getCurrentPageNo()
				|| getTotalPageCount() == 0)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("{totalSize:" + this.totalSize);
		sb.append(", totalPageCount" + this.totalPageCount);
		sb.append(", currentPageNo:" + this.currentPageNo);
		sb.append(", currentPageNo:" + this.currentPageNo);
		sb.append(", currentPageSize:" + this.currentPageSize);
		sb.append("}");
		return sb.toString();
	}
}
