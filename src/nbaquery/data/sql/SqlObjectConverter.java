package nbaquery.data.sql;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;

import nbaquery.data.Image;

public abstract class SqlObjectConverter<Type>
{
	public final Class<Type> dataClass;
	public final int sqlType;
	
	public SqlObjectConverter(Class<Type> dataClass, int sqlType)
	{
		this.dataClass = dataClass;
		this.sqlType = sqlType;
	}
	
	@SuppressWarnings("unchecked")
	public void write(PreparedStatement ps, int index, Object obj)
	{
		try {
			Type value = null;
			if(obj != null)
			{
				if(obj.getClass().equals(dataClass))
					value = (Type) obj;
				else if(obj.getClass().equals(String.class))
					value = this.convert((String) obj);
				else throw new RuntimeException("Cannot write value to field with incompatible type.");
				this.writeStatement(ps, index, value);
			}
			else ps.setNull(index, sqlType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void writeStatement(PreparedStatement ps, int index, Type value) throws Exception;
	
	protected abstract Type convert(String string);
	
	protected abstract Type read(ResultSet resultSet, int index) throws Exception;
	
	public static final HashMap<Class<?>, SqlObjectConverter<?>> converters = new HashMap<Class<?>, SqlObjectConverter<?>>();
	
	static
	{
		converters.put(String.class, new SqlObjectConverter<String>(String.class, Types.CHAR)
		{

			@Override
			protected void writeStatement(PreparedStatement ps, int index, String value) throws Exception {
				ps.setString(index, value);
			}

			@Override
			protected String convert(String string) {
				return string;
			}

			@Override
			protected String read(ResultSet resultSet, int index) throws Exception
			{
				return resultSet.getString(index);
			}
		});
		
		converters.put(Integer.class, new SqlObjectConverter<Integer>(Integer.class, Types.INTEGER)
		{
			@Override
			protected void writeStatement(PreparedStatement ps, int index, Integer value) throws Exception {
				ps.setInt(index, value);
			}

			@Override
			protected Integer convert(String string) {
				return Integer.parseInt(string);
			}

			@Override
			protected Integer read(ResultSet resultSet, int index) throws Exception {
				return resultSet.getInt(index);
			}
		});
		
		converters.put(Float.class, new SqlObjectConverter<Float>(Float.class, Types.REAL)
		{

			@Override
			protected void writeStatement(PreparedStatement ps, int index, Float value) throws Exception {
				ps.setFloat(index, value);
			}

			@Override
			protected Float convert(String string) {
				return Float.parseFloat(string);
			}

			@Override
			protected Float read(ResultSet resultSet, int index) throws Exception {
				return resultSet.getFloat(index);
			}
		});
		
		converters.put(Character.class, new SqlObjectConverter<Character>(Character.class, Types.TINYINT)
		{

			@Override
			protected void writeStatement(PreparedStatement ps, int index,
					Character value) throws Exception {
				ps.setInt(index, value);
			}

			@Override
			protected Character convert(String string)
			{
				if(string.length() > 0)
					return string.charAt(0);
				else return '\0';
			}

			@Override
			protected Character read(ResultSet resultSet, int index)
					throws Exception {
				return (char)resultSet.getInt(index);
			}
		});
		
		converters.put(Image.class, new SqlObjectConverter<Image>(Image.class, Types.CHAR)
		{

			@Override
			protected void writeStatement(PreparedStatement ps, int index,
					Image value) throws Exception {
				ps.setString(index, value.getImageFile().getAbsolutePath());
			}

			@Override
			protected Image convert(String string) {
				return new Image(new File(string));
			}

			@Override
			protected Image read(ResultSet resultSet, int index)
					throws Exception {
				return new Image(new File(resultSet.getString(index)));
			}
		});
		
		converters.put(Date.class, new SqlObjectConverter<Date>(Date.class, Types.BIGINT)
		{
			@Override
			protected void writeStatement(PreparedStatement ps, int index,
					Date value) throws Exception {
				ps.setLong(index, value.getTime());
			}

			@SuppressWarnings("deprecation")
			@Override
			protected Date convert(String string) {
				return new Date(Date.parse(string));
			}

			@Override
			protected Date read(ResultSet resultSet, int index) throws Exception {
				return new Date(resultSet.getLong(index));
			}
		});
	}
}
