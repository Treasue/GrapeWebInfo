package interfaceApplication;

import java.util.HashMap;

import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.WebModel;

/**
 * 网站信息 备注：涉及到的id都是数据表中的_id
 *
 */
@SuppressWarnings("unchecked")
public class WebInfo {
	private WebModel web = new WebModel();
	HashMap<String, Object> map = new HashMap<>();
	private JSONObject object = new JSONObject();

	public WebInfo() {
		map.put("wbid", WebModel.getID());
		map.put("ownid", 0);
		map.put("engerid", 0);
		map.put("gov", "12");
		map.put("desp", "");
		map.put("policeid", "");
		map.put("wbgid", 0);
		map.put("isdelete", 0);
		map.put("isvisble", 0);
		map.put("tid", 0);
		map.put("sort", 0);
		map.put("authid", 0);
		map.put("taskid", 0);
	}

	/**
	 * 
	 * @param webInfo
	 *          （必填字段："host", "logo", "icp", "title"）
	 * @return 1:必填数据没有填 2：ICP备案号格式错误 3:ICP已存在 4: 公安网备案号格式错误 5：title已存在
	 *         6：网站描述字数超过限制
	 */
	public String WebInsert(String webInfo) {
		JSONObject object = web.AddMap(map, JSONHelper.string2json(webInfo));
		return web.resultmessage(web.addweb(object), "新增网站信息成功");
	}

	/**
	 * 
	 * @param wbid
	 *          _id对应的值
	 * @return
	 */
	public String WebDelete(String wbid) {
		return web.resultmessage(web.delete(wbid), "删除网站信息成功");
	}

	public String WebUpdate(String wbid, String WebInfo) {
		return web.resultmessage(web.update(wbid, JSONHelper.string2json(WebInfo)),
				"网站信息更新成功");
	}

	// public String WebShow(){
	// return web.select().toJSONString();
	// }
	public String Webfind(String wbinfo) {
		JSONObject object = new JSONObject();
		object.put("records", web.select(wbinfo));
		return web.resultmessage(0, object.toString());
	}

	public String WebPage(int idx, int pageSize) {
		object.put("records", web.page(idx, pageSize));
		return web.resultmessage(0, object.toString());
	}

	public String WebPageBy(int idx, int pageSize, String webinfo) {
		JSONObject object = new JSONObject();
		object.put("records", web.page(webinfo, idx, pageSize));
		return web.resultmessage(0, object.toString());
	}

	public String WebSort(String wbid, int num) {
		return web.resultmessage(web.sort(wbid, num), "排序值设置成功");
	}

	public String WebSetwbg(String wbid, String wbgid) {
		return web.resultmessage(web.setwbgid(wbid, wbgid), "站点设置成功");
	}

	public String setTemp(String wbid, String tempid) {
		return web.resultmessage(web.settempid(wbid, tempid), "设置模版成功");
	}

	public String WebBatchDelete(String wbid) {
		return web.resultmessage(web.delete(wbid.split(",")), "批量删除成功");
	}
}
