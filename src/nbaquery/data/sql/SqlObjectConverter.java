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
	
	public abstract Type read(ResultSet resultSet, int index) throws Exception;
	
	@SuppressWarnings("unchecked")
	public void update(ResultSet rs, int index, Object obj)
	{
		try {
			Type value = null;
			if(obj != null)
			{
				if(obj.getClass().equals(dataClass))
					value = (Type) obj;
				else if(obj.getClass().equals(String.class))
					value = this.convert((String) obj);
				else throw new RuntimeException("Cannot update value to field with incompatible type.");
				this.updateStatement(rs, index, value);
			}
			else rs.updateNull(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void updateStatement(ResultSet rs, int index, Type value) throws Exception;
	
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
			public String read(ResultSet resultSet, int index) throws Exception
			{
				return resultSet.getString(index);
			}

			@Override
			protected void updateStatement(ResultSet rs, int index, String value)
					throws Exception {
				rs.updateString(index, value);
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
			public Integer read(ResultSet resultSet, int index) throws Exception {
				return resultSet.getInt(index);
			}

			@Override
			protected void updateStatement(ResultSet rs, int index,
					Integer value) throws Exception {
				rs.updateInt(index, value);
			}
		});
		
		converters.put(Float.class, new SqlObjectConverter<Float>(Float.class, Types.REAL)
		{

			@Override
			protected void writeStatement(PreparedStatement ps, int index, Float value) throws Exception {
				if(Float.isInfinite(value) || Float.isNaN(value))
					ps.setNull(index, sqlType);
				else ps.setFloat(index, value);
			}

			@Override
			protected Float convert(String string) {
				return Float.parseFloat(string);
			}

			@Override
			public Float read(ResultSet resultSet, int index) throws Exception {
				return resultSet.getFloat(index);
			}

			@Override
			protected void updateStatement(ResultSet rs, int index, Float value)
					throws Exception {
				rs.updateFloat(index, value);
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
			public Character read(ResultSet resultSet, int index)
					throws Exception {
				return (char)resultSet.getInt(index);
			}

			@Override
			protected void updateStatement(ResultSet rs, int index,
					Character value) throws Exception {
				rs.updateInt(index, value);
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
			public Image read(ResultSet resultSet, int index)
					throws Exception {
				return new Image(new File(resultSet.getString(index)));
			}

			@Override
			protected void updateStatement(ResultSet rs, int index, Image value)
					throws Exception {
				rs.updateString(index, value.getImageFile().getAbsolutePath());
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
			public Date read(ResultSet resultSet, int index) throws Exception {
				return new Date(resultSet.getLong(index));
			}

			@Override
			protected void updateStatement(ResultSet rs, int index, Date value)
					throws Exception {
				rs.updateLong(index, value.getTime());
			}
		});
	}
}
