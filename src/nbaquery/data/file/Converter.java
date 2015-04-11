package nbaquery.data.file;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Converter<T>
{
	public static class ConverterMap
	{
		public static final Map<Class<?>, Converter<?>> conversion = new HashMap<Class<?>, Converter<?>>();
		static
		{
			conversion.put(String.class, new Converter<String>()
			{
				@Override
				public String convert(String input)
				{
					return StringPool.createSeasonFromPool(input);
					//return input;
				}
			});
			
			conversion.put(Integer.class, new Converter<Integer>()
			{
				@Override
				public Integer convert(String input)
				{
					try
					{
						return Integer.parseInt(input);
					}
					catch(Exception e)
					{
						return null;
					}
				}
			});
			conversion.put(Character.class, new Converter<Character>()
			{
				@Override
				public Character convert(String input)
				{
					if(input.length() == 0) return '\0';
					else return input.charAt(0);
				}
			});
			conversion.put(Date.class, new Converter<Date>()
			{

				@SuppressWarnings("deprecation")
				@Override
				public Date convert(String input)
				{
					return new Date(input);
				}
			});
		}
	}
	
	public T convert(String input);
}
