<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="MySqlMapper">



    <select id="${procedureName}" statementType="CALLABLE" parameterType="map" resultMap="BaseResultMap">
        <![CDATA[
        {
    <#--${r'#{d, mode=OUT, jdbcType=INTEGER}'} =-->
        call ${procedureName}(
    ${r'#{a, mode=IN, jdbcType=INTEGER}'}
        )
        }
        ]]>
    </select>

</mapper>