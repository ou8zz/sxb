package com.sxb.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @description SQL Date handler class.
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date Aug 7, 2011 11:04:25 PM
 * @version 3.0
 */
public class SQLDateSerializer implements JsonSerializer<Date>,JsonDeserializer<Date> {
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public JsonElement serialize(Date src, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(format.format(src.getTime()));
	}

	@Override
	public Date deserialize(JsonElement json, Type typeofT,JsonDeserializationContext context) throws JsonParseException {
		if(!(json instanceof JsonPrimitive)) {
			throw new JsonParseException("The date should be a string value");
		}
		try {
			Date date = format.parse(json.getAsString());
			return new Date(date.getTime());
		}catch(ParseException e) {
			throw new JsonParseException(e);
		}
	}
}
