package interfaceApplication;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import esayhelper.JSONHelper;
import model.WebGroupModel;
import model.WebModel;

/**
 * 站群 备注：涉及到的id，都是数据表中的_id 
 * 返回数据类型为{"message":显示数据或操作提示,"errorcode":错误码}
 * 错误码为0，表示数据或操作正常
 *
 */
public class WebGroup {
	private WebGroupModel webgroup = new WebGroupModel();
	private WebModel webModel = new WebModel();
	private HashMap<String, Object> defmap = new HashMap<>();

	public WebGroup() {
		defmap.put("ownid", 1);
		defmap.put("sort", 0);
		defmap.put("fatherid", 0); // 默认fatherid为0，为一级站群
		defmap.put("wbgid", "0");
	}

	public String WebGroupInsert(String webgroupInfo) {
		JSONObject object = webgroup.AddMap(defmap, JSONHelper.string2json(webgroupInfo));
		return webgroup.resultmessage(webgroup.add(object), "站群新增成功");
	}

	public String WebGroupDelete(String id) {
		int code = 0;
		String jsondtring = "{\"wbgid\":\"0\"}";
		if (webModel.selectbyid(id).size() != 0) {
			code = webModel.updatebywbgid(id, JSONHelper.string2json(jsondtring));
		}
		if (code == 0) {
			code = webgroup.delete(id);
		}
		return webgroup.resultmessage(code, "站群删除成功");
	}

	@SuppressWarnings("unchecked")
	public String WebGroupSelect() {
		JSONObject object = new JSONObject();
		object.put("records", webgroup.search());
		return webgroup.resultmessage(0, object.toString());
	}

	@SuppressWarnings("unchecked")
	public String WebGroupFind(String webinfo) {
		JSONObject object = new JSONObject();
		object.put("records", webgroup.select(webinfo));
		return webgroup.resultmessage(0, object.toString());
	}

	public String WebGroupUpdate(String wbgid, String webgroupInfo) {
		return webgroup.resultmessage(webgroup.update(wbgid, webgroupInfo), "站群修改成功");
	}

	@SuppressWarnings("unchecked")
	public String WebGroupPage(int idx, int pageSize) {
		JSONObject _obj = new JSONObject();
		_obj.put("records", webgroup.page(idx, pageSize));
		return webgroup.resultmessage(0, _obj.toString());
	}

	@SuppressWarnings("unchecked")
	public String WebGroupPageBy(int idx, int pageSize, String webinfo) {
		JSONObject _obj = new JSONObject();
		_obj.put("records", webgroup.page(webinfo, idx, pageSize));
		return webgroup.resultmessage(0, _obj.toString());
	}

	public String WebGroupUpBatch(String arraystring) {
		int code = 0;
		JSONArray array = (JSONArray) JSONValue.parse(arraystring);
		for (int i = 0; i < array.size(); i++) {
			if (code != 0) {
				return webgroup.resultmessage(3, "");
			}
			JSONObject object = (JSONObject) array.get(i);
			code = webgroup.sortAndFatherid(object);
		}
		return webgroup.resultmessage(code, "设置排序值或层级成功");
	}

	/**
	 * 修改排序值
	 * 
	 * @param webinfo包括{"_id":"value","sort":x}
	 * @return
	 */
	public String WebGroupSort(String webinfo) {
		return webgroup.resultmessage(webgroup.sortAndFatherid(JSONHelper.string2json(
				webinfo)), "设置排序值成功");
	}

	/**
	 * 设置上级站群，默认fatherid为0时，是一级站群
	 * 
	 * @param webinfo包括{"_id":"value","fatherid":x}
	 * @return
	 */
	public String WebGroupSetfatherid(String webinfo) {
		return webgroup.resultmessage(webgroup.sortAndFatherid(JSONHelper.string2json(
				webinfo)), "设置上级站群成功");
	}

	public String WebGroupBatchDelete(String arrys) {
		return webgroup.resultmessage(webgroup.delete(arrys.split(",")), "批量删除成功");
	}
}
