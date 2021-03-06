/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Nov 9, 20178:37:14 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsBackMapper;
import com.zm.goods.bussiness.dao.GoodsBaseMapper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ERPGoodsTagBindEntity;
import com.zm.goods.pojo.ERPGoodsTagEntity;
import com.zm.goods.pojo.GoodsBaseEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsInfoEntity;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.TagFuncEntity;
import com.zm.goods.pojo.ThirdWarehouseGoods;

/**
 * ClassName: GoodsBackServiceImpl <br/>
 * Function: 品牌服务. <br/>
 * date: Nov 9, 2017 8:37:14 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class GoodsBackServiceImpl implements GoodsBackService {

	@Resource
	GoodsBackMapper goodsBackMapper;

	@Resource
	GoodsItemMapper goodsItemMapper;

	@Resource
	GoodsBaseMapper goodsBaseMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Override
	public Page<GoodsEntity> queryByPage(GoodsEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectForPage(entity);
	}

	@Override
	public GoodsEntity queryById(int id) {
		GoodsEntity entity = goodsBackMapper.selectById(id);
		List<GoodsFile> fileList = goodsBackMapper.selectGoodsFileByGoodsId(entity);
		GoodsEntity entityWithItem = goodsBackMapper.selectGoodsWithItem(id);
		ERPGoodsTagBindEntity tagBind = goodsBackMapper.selectGoodsTagBindByGoodsId(entityWithItem.getGoodsItem());
		entity.setFiles(fileList);
		entity.setGoodsTagBind(tagBind);
		return entity;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void save(GoodsEntity entity, String type) {
		goodsBackMapper.insert(entity);
		goodsItemMapper.insert(entity.getGoodsItem());
		goodsItemMapper.insertPrice(entity.getGoodsItem().getGoodsPrice());
		if (entity.getFiles() != null && entity.getFiles().size() > 0) {
			goodsItemMapper.insertFiles(entity.getFiles());
		}

		if ("sync".equals(type)) {
			goodsBackMapper.updateThirdStatus(entity.getThirdId());
		}
		if (entity.getGoodsTagBind() != null) {
			goodsBackMapper.insertTagBind(entity.getGoodsTagBind());
		}
	}

	@Override
	public Page<ThirdWarehouseGoods> queryByPage(ThirdWarehouseGoods entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectThirdForPage(entity);
	}

	@Override
	public ThirdWarehouseGoods queryThird(ThirdWarehouseGoods entity) {
		return goodsBackMapper.selectThird(entity);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void edit(GoodsEntity entity) {
		goodsBackMapper.update(entity);
		List<GoodsFile> isrFileList = new ArrayList<GoodsFile>();
		if (entity.getFiles() != null && entity.getFiles().size() > 0) {
			for (GoodsFile gf : entity.getFiles()) {
				if (gf.getId() != null) {
					goodsItemMapper.updateFiles(gf);
				} else {
					isrFileList.add(gf);
				}
			}
		}
		if (isrFileList != null && isrFileList.size() > 0) {
			goodsItemMapper.insertFiles(isrFileList);
		}
		// 判断商品标签
		// if (entity.getGoodsTagBind() != null) {
		// //增删改
		// ERPGoodsTagBindEntity oldTag =
		// goodsBackMapper.selectGoodsTagBindByGoodsId(entity.getGoodsItem());
		// ERPGoodsTagBindEntity newTag = entity.getGoodsTagBind();
		// if (oldTag == null && newTag.getTagId() != 0) {
		// goodsBackMapper.insertTagBind(newTag);
		// } else if (oldTag != null && newTag.getTagId() == 0) {
		// goodsBackMapper.deleteTagBind(newTag);
		// } else if (oldTag != null && newTag.getTagId() != 0) {
		// goodsBackMapper.updateTagBind(newTag);
		// }
		// }
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void remove(GoodsEntity entity) {
		goodsBackMapper.delete(entity);
		goodsItemMapper.delete(entity);
	}

	@Override
	public GoodsEntity checkRecordForDel(GoodsEntity entity) {
		GoodsEntity retEntity = goodsBackMapper.selectRecordForDel(entity);
		return retEntity;
	}

	@Override
	public void saveDetailPath(GoodsEntity entity) {
		goodsBackMapper.updateDetailPath(entity);
	}

	@Override
	public GoodsEntity checkRecordForUpd(GoodsEntity entity) {
		GoodsEntity retEntity = goodsBackMapper.selectRecordForUpd(entity);
		return retEntity;
	}

	@Override
	public Page<GoodsRebateEntity> queryAllGoods(GoodsEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectAllGoodsForRebate(entity);
	}

	@Override
	public List<GoodsRebateEntity> queryById(String itemId) {
		return goodsBackMapper.selectGoodsRebateById(itemId);
	}

	@Override
	public GoodsRebateEntity checkRecordForRebate(GoodsRebateEntity entity) {
		return goodsBackMapper.selectRecordForRebate(entity);
	}

	@Override
	public void insertGoodsRebate(List<GoodsRebateEntity> entityList) {
		goodsBackMapper.insertGoodsRebate(entityList);
		setRedis(entityList);
	}

	private void setRedis(List<GoodsRebateEntity> entityList) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> result = new HashMap<String, String>();
		for (GoodsRebateEntity entity : entityList) {
			result.put(entity.getGradeType() + "", entity.getProportion() + "");
		}

		hashOperations.putAll(Constants.GOODS_REBATE + entityList.get(0).getItemId(), result);
	}

	@Override
	public void updateGoodsRebate(GoodsRebateEntity entity) {
		// goodsBackMapper.updateGoodsRebate(entity);
		// setRedis(entity);
	}

	@Override
	public Page<ERPGoodsTagEntity> queryTagForPage(ERPGoodsTagEntity entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return goodsBackMapper.selectTagForPage(entity);
	}

	@Override
	public void insertGoodsTag(ERPGoodsTagEntity entity) {
		goodsBackMapper.insertGoodsTag(entity);
	}

	@Override
	public void updateGoodsTag(ERPGoodsTagEntity entity) {
		goodsBackMapper.updateGoodsTag(entity);
	}

	@Override
	public void deleteGoodsTag(ERPGoodsTagEntity entity) {
		goodsBackMapper.deleteGoodsTag(entity);
	}

	@Override
	public ERPGoodsTagEntity queryTagInfo(ERPGoodsTagEntity entity) {
		return goodsBackMapper.selectTagInfo(entity);
	}

	@Override
	public List<ERPGoodsTagEntity> queryTagListInfo() {
		return goodsBackMapper.selectTagListInfo();
	}

	@Override
	public List<TagFuncEntity> queryTagFuncList() {
		return goodsBackMapper.selectTagFuncListInfo();
	}

	@Override
	public List<ERPGoodsTagBindEntity> queryGoodsTagBindListInfo(ERPGoodsTagBindEntity entity) {
		return goodsBackMapper.selectGoodsTagBindListInfo(entity);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void saveGoodsInfo(GoodsInfoEntity entity) {
		if (entity.getGoodsBase().getId() != 0) {
			goodsBaseMapper.insert(entity.getGoodsBase());
		}
		goodsBackMapper.insert(entity.getGoods());
		goodsItemMapper.insert(entity.getGoods().getGoodsItem());
		goodsItemMapper.insertPrice(entity.getGoods().getGoodsItem().getGoodsPrice());
		if (entity.getGoods().getFiles() != null && entity.getGoods().getFiles().size() > 0) {
			goodsItemMapper.insertFiles(entity.getGoods().getFiles());
		}
		if (entity.getGoods().getGoodsTagBind() != null) {
			goodsBackMapper.insertTagBind(entity.getGoods().getGoodsTagBind());
		}
	}

	@Override
	public GoodsInfoEntity queryGoodsInfoEntity(String itemId) {
		GoodsInfoEntity goodsInfo = new GoodsInfoEntity();
		// 查询商品信息
		GoodsItemEntity goodsItemEntity = goodsBackMapper.selectGoodsItemByItemId(itemId);
		GoodsPrice goodsPrice = goodsItemMapper.selectItemPrice(itemId);
		GoodsEntity goodsEntity = goodsBackMapper.selectGoodsEntityByItemId(goodsItemEntity.getGoodsId());
		List<GoodsFile> goodsFiles = goodsBackMapper.selectGoodsFileByGoodsId(goodsEntity);
		ERPGoodsTagBindEntity erpGoodsTagBind = goodsBackMapper.selectGoodsTagBindByGoodsId(goodsItemEntity);
		GoodsBaseEntity goodsBaseEntity = goodsBaseMapper.selectById(goodsEntity.getBaseId());
		// 组装商品信息
		goodsItemEntity.setGoodsPrice(goodsPrice);
		goodsEntity.setFiles(goodsFiles);
		goodsEntity.setGoodsTagBind(erpGoodsTagBind);
		goodsEntity.setGoodsItem(goodsItemEntity);
		goodsInfo.setGoods(goodsEntity);
		goodsInfo.setGoodsBase(goodsBaseEntity);
		return goodsInfo;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void updateGoodsInfo(GoodsInfoEntity entity) {
		goodsBackMapper.updateGoodsEntity(entity.getGoods());
		goodsItemMapper.update(entity.getGoods().getGoodsItem());
		goodsItemMapper.updatePrice(entity.getGoods().getGoodsItem().getGoodsPrice());
		if (entity.getGoods().getFiles() != null && entity.getGoods().getFiles().size() > 0) {
			//商品编辑时，先查询原有的file数据进行比较，然后判断如何处理
			List<GoodsFile> oldFiles = goodsBackMapper.selectGoodsFileByGoodsId(entity.getGoods());
			List<GoodsFile> existFiles = new ArrayList<GoodsFile>();

			//过滤相同文件列表
			for(GoodsFile ngf : entity.getGoods().getFiles()) {
				for(GoodsFile gf : oldFiles) {
					if (ngf.getGoodsId().equals(gf.getGoodsId()) && ngf.getPath().equals(gf.getPath())) {
						existFiles.add(gf);
						oldFiles.remove(gf);
						break;
					}
				}
			}
			//挑出新增文件列表
			for(GoodsFile gf : existFiles) {
				for(GoodsFile ngf : entity.getGoods().getFiles()) {
					if (ngf.getGoodsId().equals(gf.getGoodsId()) && ngf.getPath().equals(gf.getPath())) {
						entity.getGoods().getFiles().remove(ngf);
						break;
					}
				}
			}
			
			if (entity.getGoods().getFiles().size() > 0) {
				goodsItemMapper.insertFiles(entity.getGoods().getFiles());
			}
			if (oldFiles.size() > 0) {
				goodsItemMapper.deleteListFiles(oldFiles);
			}
		} else {
			//商品编辑时，如果没有传图片信息，则删除表中记录
			goodsItemMapper.deleteAllFiles(entity.getGoods());
		}
		// 判断商品标签
		 ERPGoodsTagBindEntity oldTag = goodsBackMapper.selectGoodsTagBindByGoodsId(entity.getGoods().getGoodsItem());
		 if (entity.getGoods().getGoodsTagBind() != null) {
			 //增删改
			 ERPGoodsTagBindEntity newTag = entity.getGoods().getGoodsTagBind();
			 if (oldTag == null && newTag.getTagId() != 0) {
				 goodsBackMapper.insertTagBind(newTag);
			 } else if (oldTag != null && newTag.getTagId() == 0) {
				 goodsBackMapper.deleteTagBind(oldTag);
			 } else if (oldTag != null && newTag.getTagId() != 0) {
				 goodsBackMapper.updateTagBind(newTag);
			 }
		 } else {
			 if (oldTag != null) {
				 goodsBackMapper.deleteTagBind(oldTag);
			 }
		 }
	}

	@Override
	public ResultModel getGoodsRebate(String itemId) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		return new ResultModel(true, hashOperations.entries(Constants.GOODS_REBATE + itemId));
	}
}
