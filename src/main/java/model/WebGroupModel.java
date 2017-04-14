package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.JSONHelper;
import esayhelper.formHelper;
import esayhelper.formHelper.formdef;
import esayhelper.jGrapeFW_Message;

@SuppressWarnings("unchecked")
public class WebGroupModel {
	private static DBHelper dbwebgroup;
	private static formHelper _form;
	static {
		dbwebgroup = new DBHelper("mongodb", "webgroup");
		_form = dbwebgroup.getChecker();
	}

	public WebGroupModel() {
		_form.putRule("name", formdef.notNull);
	}

	/**
	 * 新增站群
	 * 
	 * @param webgroupInfo
	 * @return 0：添加数据成功 1：存在非空字段 2：存在同名站群 其它异常
	 */
	public int add(JSONObject webgroupInfo) {
		if (!_form.checkRuleEx(webgroupInfo)) {
			return 1; // 必填字段没有填
		}
		// 判断库中是否存在同名站群
		String name = webgroupInfo.get("name").toString();
		if (findByName(name) != null) {
			return 2;
		}
		return dbwebgroup.data(webgroupInfo).insertOnce() != null ? 0 : 99;
	}

	public int delete(String id) {
		return dbwebgroup.eq("_id", new ObjectId(id)).delete() != null ? 0 : 99;
	}

	public JSONArray search() {
		return dbwebgroup.select();
	}

	public JSONArray select(String webinfo) {
		JSONObject object = JSONHelper.string2json(webinfo);
		Set<Object> set = object.keySet();
		for (Object object2 : set) {
			if (object2.equals("_id")) {
				dbwebgroup.eq("_id", new ObjectId(object.get(object2.toString()).toString()));
			}
			dbwebgroup.eq(object2.toString(), object.get(object2.toString()));
		}
		return dbwebgroup.select();
	}

	public int update(String wbgid, String webinfo) {
		// dbwebgroup.protectfield(field);
		JSONObject _webinfo = JSONHelper.string2json(webinfo);
		System.out.println(_webinfo);
		// 非空字段判断
		if (!_form.checkRule(_webinfo)) {
			return 1;
		}
		if (_webinfo.containsKey("name")) {
			String name = _webinfo.get("name").toString();
			if (findByName(name) != null) {
				return 2;
			}
		}
		JSONObject object = dbwebgroup.eq("_id", new ObjectId(wbgid)).data(_webinfo).update();
		return object != null ? 0 : 99;
	}

	public int sortAndFatherid(JSONObject object) {
		JSONObject _obj = new JSONObject();
		if (object.containsKey("sort")) {
			_obj.put("sort", object.get("sort").toString());
		}
		if (object.containsKey("fatherid")) {
			_obj.put("fatherid", object.get("fatherid").toString());
		}
		return dbwebgroup.eq("_id", new ObjectId(object.get("_id").toString())).data(_obj).update() != null ? 0 : 99;
	}

	public JSONObject page(int idx, int pageSize) {
		JSONArray array = dbwebgroup.page(idx, pageSize);
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;

			{
				put("totalSize", (int) Math.ceil((double) dbwebgroup.count() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);

			}
		};
		return object;
	}

	public JSONObject page(String webinfo, int idx, int pageSize) {
		Set<Object> set = JSONHelper.string2json(webinfo).keySet();
		for (Object object2 : set) {
			dbwebgroup.eq(object2.toString(), JSONHelper.string2json(webinfo).get(object2.toString()));
		}
		JSONArray array = dbwebgroup.page(idx, pageSize);
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;
			{
				put("totlsize", (int) Math.ceil((double) dbwebgroup.count() / pageSize));
				put("currentPage", idx);
				put("PageSize", pageSize);
				put("data", array);

			}
		};
		return object;
	}

	public JSONObject findByName(String name) {
		return dbwebgroup.eq("name", name).find();
	}

	public String findbyfatherid(String fatherid) {
		JSONArray array = dbwebgroup.eq("fatherid", fatherid).select();
		JSONObject _obj;
		String name = null;
		for (Object object : array) {
			_obj = (JSONObject) object;
			name = _obj.get("name").toString();
		}
		return name;
	}

	// public String setfatherid(String wbgid, String fathrid) {
	// JSONObject _obj = new JSONObject();
	// _obj.put("fatherid", fathrid);
	// return dbwebgroup.eq("_id", new
	// ObjectId(wbgid)).data(_obj).update().toString();
	// }

	public int delete(String[] arr) {
		dbwebgroup = (DBHelper) dbwebgroup.or();
		for (int i = 0; i < arr.length; i++) {
			dbwebgroup.eq("_id", arr[i]);
		}
		return dbwebgroup.delete() != null ? 0 : 99;
	}

	/**
	 * 生成32位随机编码
	 * 
	 * @return
	 */
	public static String getID() {
		String str = UUID.randomUUID().toString().trim();
		return str.replace("-", "");
	}

	/**
	 * 将map添加至JSONObject中
	 * 
	 * @param map
	 * @param object
	 * @return
	 */
	public JSONObject AddMap(HashMap<String, Object> map, JSONObject object) {
		if (map.entrySet() != null) {
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
				if (!object.containsKey(entry.getKey())) {
					object.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return object;
	}

	public String resultmessage(int num, String message) {
		String msg = "";
		switch (num) {
		case 0:
			msg = message;
			break;
		case 1:
			msg = "必填字段没有填";
			break;
		case 2:
			msg = "已存在该站群";
			break;
		case 3:
			msg = "批量操作失败";
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
