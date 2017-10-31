<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${mapperClassName}">

    <#if cache?? && cache == "MYBATIS">
        <cache eviction="LRU" flushInterval="60000" size="1024"  readOnly="false"></cache>
    </#if>

    <resultMap id="BaseResultMap" type="${entityClassName}">
    <#list entityClassColumns as column>
        <#if column.id>
            <id column="${column.column}" <#if column.jdbcTypeName??>jdbcType="${column.jdbcTypeName}"</#if> property="${column.property}"/>
        </#if>
    </#list>
    <#list entityClassColumns as column>
        <#if !column.id>
            <result column="${column.column}" <#if column.jdbcTypeName??>jdbcType="${column.jdbcTypeName}"</#if> property="${column.property}"/>
        </#if>
    </#list>
    <#list mybatisAssociations as association>
        <association property="${association.property}" javaType="${association.targetEntityTable.entityClassName}">
            <#list association.resultMap as result>
                <#if result.id>
                    <id column="${association.property}_${result.column}" <#if result.jdbcTypeName??>jdbcType="${result.jdbcTypeName}"</#if> property="${result.property}"/>
                </#if>
            </#list>
            <#list association.resultMap as result>
                <#if !result.id>
                    <result column="${association.property}_${result.column}" <#if result.jdbcTypeName??>jdbcType="${result.jdbcTypeName}"</#if> property="${result.property}"/>
                </#if>
            </#list>
        </association>
    </#list>
    <#list mybatisConnections as connection>
        <collection property="${connection.property}" ofType="${connection.targetEntityTable.entityClassName}">
            <#list connection.resultMap as result>
                <#if result.id>
                    <id column="${connection.property}_${result.column}" <#if result.jdbcTypeName??>jdbcType="${result.jdbcTypeName}"</#if> property="${result.property}"/>
                </#if>
            </#list>
            <#list connection.resultMap as result>
                <#if !result.id>
                    <result column="${connection.property}_${result.column}" <#if result.jdbcTypeName??>jdbcType="${result.jdbcTypeName}"</#if> property="${result.property}"/>
                </#if>
            </#list>
        </collection>
    </#list>
    </resultMap>

    <insert id="insert" parameterType="${entityClassName}" useGeneratedKeys="true" keyProperty="<#list entityClassPKColumns as column>${column.property}</#list>">
        insert into ${name}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list entityClassColumns as column>
            <#if column.id>
                <if test="${column.property} != null">
                ${column.column},
                </if>
            <#else >
            ${column.column},
            </#if>
        </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
        <#list entityClassColumns as column>
            <#if column.id>
                <if test="${column.property} != null">
                ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
                </if>
            <#else >
                <if test="${column.property} != null">
                ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
                </if>
                <if test="${column.property} == null">
                    DEFAULT,
                </if>
            </#if>
        </#list>
        </trim>
    </insert>

    <insert id="insertList" parameterType="java.util.ArrayList" useGeneratedKeys="true" keyProperty="<#list entityClassPKColumns as column>${column.property}</#list>" keyColumn="<#list entityClassPKColumns as column>${column.property}</#list>">
        insert into ${name}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list entityClassColumns as column>
            ${column.column},
        </#list>
        </trim>
        VALUES
        <foreach collection="list" item="record" index="index" separator=",">
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list entityClassColumns as column>
            <if test="record.${column.property} != null">
            ${r'#{'}record.${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
            </if>
            <if test="record.${column.property} == null">
                DEFAULT,
            </if>
        </#list>
        </trim>
        </foreach>
    </insert>

    <update id="updateByPrimaryKey" parameterType="${entityClassName}">
        update ${name}
        <trim prefix=" set " suffix=" " suffixOverrides=",">
        <#list entityClassColumns as column>
            <#if !column.id>
            ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
            </#if>
        </#list>
        </trim>
        <trim prefix=" where " suffix=" " suffixOverrides="and">
        <#list entityClassPKColumns as column>
        ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'} and
        </#list>
        </trim>
    </update>

    <update id="updateByPrimaryKeySelective" parameterType="${entityClassName}">
        update ${name}
        <trim prefix=" set " suffix=" " suffixOverrides=",">
        <#list entityClassColumns as column>
            <#if !column.id>
                <if test="${column.property} != null">
                ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
                </if>
            </#if>
        </#list>
        </trim>
        <trim prefix=" where " suffix=" " suffixOverrides="and">
        <#list entityClassPKColumns as column>
        ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'} and
        </#list>
        </trim>
    </update>

    <update id="updateSelectiveByPrimaryKey" parameterType="${entityClassName}">
        update ${name}
        <trim prefix=" set " suffix=" " suffixOverrides=",">
        <#list entityClassColumns as column>
            <#if !column.id>
                <if test="${column.property} != null">
                ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
                </if>
            </#if>
        </#list>
        </trim>
        <trim prefix=" where " suffix=" " suffixOverrides="and">
        <#list entityClassPKColumns as column>
        ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'} and
        </#list>
        </trim>
    </update>

    <update id="updateByParams" parameterType="${entityClassName}">
        update ${name}
        <trim prefix=" set " suffix=" " suffixOverrides=",">
        <#list entityClassColumns as column>
            <#if !column.id>
            ${column.column} = ${r'#{record.'}${column.property}<#if column.jdbcTypeName??>,jdbcType=${column.jdbcTypeName}</#if>${r'}'},
            </#if>
        </#list>
        </trim>
        <if test="inParams != null">
            <where>
                <foreach collection="inParams.orCriteria" item="criteria" separator="or">
                    <if test="criteria.valid">
                        <trim prefix="(" prefixOverrides="and" suffix=")">
                            <foreach collection="criteria.criteria" item="criterion">
                                <choose>
                                    <when test="criterion.noValue">
                                        and ${r'${criterion.condition}'}
                                    </when>
                                    <when test="criterion.singleValue">
                                        and ${r'${criterion.condition} #{criterion.value}'}
                                    </when>
                                    <when test="criterion.betweenValue">
                                        and ${r'${criterion.condition} #{criterion.value} and #{criterion.secondValue}'}
                                    </when>
                                    <when test="criterion.listValue">
                                        and ${r'${criterion.condition}'}
                                        <foreach close=")" collection="criterion.value" item="listItem" open="("
                                                 separator=",">
                                        ${r'#{listItem}'}
                                        </foreach>
                                    </when>
                                </choose>
                            </foreach>
                        </trim>
                    </if>
                    <if test="criteria.valid == false">
                        1 = -1
                    </if>
                </foreach>
            </where>
        </if>
        <if test = "inParams == null">
            <where>
                1 = -1
            </where>
        </if>

    </update>

    <update id="updateSelectiveByParams">
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasValue(record) == true">
            update ${name}
            <trim prefix=" set " suffix=" " suffixOverrides=",">
            <#list entityClassColumns as column>
                <if test="record.${column.property} != null">
                ${column.column} = ${r'#{record.'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'},
                </if>
            </#list>
            </trim>
            <if test="inParams != null">
                <where>
                    <foreach collection="inParams.orCriteria" item="criteria" separator="or">
                        <if test="criteria.valid">
                            <trim prefix="(" prefixOverrides="and" suffix=")">
                                <foreach collection="criteria.criteria" item="criterion">
                                    <choose>
                                        <when test="criterion.noValue">
                                            and ${r'${criterion.condition}'}
                                        </when>
                                        <when test="criterion.singleValue">
                                            and ${r'${criterion.condition} #{criterion.value}'}
                                        </when>
                                        <when test="criterion.betweenValue">
                                            and ${r'${criterion.condition} #{criterion.value} and #{criterion.secondValue}'}
                                        </when>
                                        <when test="criterion.listValue">
                                            and ${r'${criterion.condition}'}
                                            <foreach close=")" collection="criterion.value" item="listItem" open="("
                                                     separator=",">
                                            ${r'#{listItem}'}
                                            </foreach>
                                        </when>
                                    </choose>
                                </foreach>
                            </trim>
                        </if>
                        <if test="criteria.valid == false">
                            1 = -1
                        </if>
                    </foreach>
                </where>
            </if>
            <if test = "inParams == null">
                <where>
                    1 = -1
                </where>
            </if>
        </if>
    </update>

    <delete id="deleteByPrimaryKey" parameterType="java.lang.Long">
        delete from ${name}
        <trim prefix=" where " suffix=" " suffixOverrides="and">
        <#list entityClassPKColumns as column>
        ${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'} and
        </#list>
        </trim>
    </delete>

    <delete id="deleteByParams" parameterType="com.czy.seed.mybatis.base.QueryParams">
        delete from ${name}
        <if test="_parameter != null">
            <where>
                <foreach collection="orCriteria" item="criteria" separator="or">
                    <include refid="criteriaIsValid"/>
                    <include refid="criteriaIsNotValid"/>
                </foreach>
            </where>
        </if>
        <if test="_parameter == null">
            <where>
                1 = -1
            </where>
        </if>
    </delete>

    <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="long">
        select
        <trim prefix=" " suffix=" " suffixOverrides=",">
        <#list entityClassColumns as column>
        ${name}.${column.column},
        </#list>
        </trim>
        from ${name}
        <trim prefix=" where " suffix=" " suffixOverrides="and">
        <#list entityClassPKColumns as column>
        ${name}.${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'} and
        </#list>
        </trim>
    </select>

    <select id="selectRelativeByPrimaryKey" resultMap="BaseResultMap" parameterType="long">
        select
        <trim prefix=" " suffix=" " suffixOverrides=",">
        <#list entityClassColumns as column>
        ${name}.${column.column},
        </#list>
        <#list mybatisAssociations as association>
            <#list association.resultMap as result>
            ${association.targetTableName}_${association.property}.${result.column} as ${association.property}_${result.column},
            </#list>
        </#list>
        <#list mybatisConnections as connection>
            <#list connection.resultMap as result>
            ${connection.targetTableName}_${connection.property}.${result.column} as ${connection.property}_${result.column},
            </#list>
        </#list>
        </trim>
        from ${name}
    <#list mybatisAssociations as association>
    ${association.joinCondition}
    </#list>
    <#list mybatisConnections as connection>
    ${connection.joinCondition}
    </#list>
        <trim prefix=" where " suffix=" " suffixOverrides="and">
        <#list entityClassPKColumns as column>
        ${name}.${column.column} = ${r'#{'}${column.property}<#if column.jdbcTypeName??>, jdbcType=${column.jdbcTypeName}</#if>${r'}'} and
        </#list>
        </trim>
    </select>

    <select id="selectListByParams" resultMap="BaseResultMap" parameterType="com.czy.seed.mybatis.base.QueryParams">
        select
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter)">
            <foreach collection="_parameter.selectColumns" item="selectColumn" separator=",">${name}.${r'${selectColumn}'}</foreach>
        </if>
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter) == false">
            <trim prefix=" " suffix=" " suffixOverrides=",">
            <#list entityClassColumns as column>
            ${name}.${column.column},
            </#list>
            </trim>
        </if>
        from ${name}
        <if test="_parameter != null">
            <where>
                <foreach collection="orCriteria" item="criteria" separator="or">
                    <include refid="criteriaIsValid"/>
                </foreach>
            </where>
            <if test="_parameter.orderByClause != null">
                ORDER BY ${r'${orderByClause}'}
            </if>
        </if>
    </select>

    <select id="selectListRelativeByParams" resultMap="BaseResultMap" parameterType="com.czy.seed.mybatis.base.QueryParams">
        select
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter)">
            <foreach collection="_parameter.selectColumns" item="selectColumn" separator=",">${name}.${r'${selectColumn}'}</foreach>
        </if>
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter) == false">
            <trim prefix=" " suffix=" " suffixOverrides=",">
            <#list entityClassColumns as column>
            ${name}.${column.column},
            </#list>
            <#list mybatisAssociations as association>
                <#list association.resultMap as result>
                ${association.targetTableName}_${association.property}.${result.column} as ${association.property}_${result.column},
                </#list>
            </#list>
            <#list mybatisConnections as connection>
                <#list connection.resultMap as result>
                ${connection.targetTableName}_${connection.property}.${result.column} as ${connection.property}_${result.column},
                </#list>
            </#list>
            </trim>
        </if>
        from ${name}
        <#list mybatisAssociations as association>
            ${association.joinCondition}
        </#list>
        <#list mybatisConnections as connection>
             ${connection.joinCondition}
        </#list>
        <if test="_parameter != null">
            <where>
                <foreach collection="orCriteria" item="criteria" separator="or">
                    <include refid="criteriaIsValid"/>
                </foreach>
            </where>
            <if test="_parameter.orderByClause != null">
                ORDER BY ${r'${orderByClause}'}
            </if>
        </if>
    </select>

    <select id="selectOneByParams" resultMap="BaseResultMap" parameterType="com.czy.seed.mybatis.base.QueryParams">
        select distinct
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter)">
            <foreach collection="_parameter.selectColumns" item="selectColumn" separator=",">${name}.${r'${selectColumn}'}</foreach>
        </if>
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter) == false">
            <trim prefix=" " suffix=" " suffixOverrides=",">
            <#list entityClassColumns as column>
            ${name}.${column.column},
            </#list>
            </trim>
        </if>
        from ${name}
        <if test="_parameter != null">
            <where>
                <foreach collection="orCriteria" item="criteria" separator="or">
                    <include refid="criteriaIsValid"/>
                </foreach>
            </where>
        </if>
    </select>

    <select id="selectOneRelativeByParams" resultMap="BaseResultMap" parameterType="com.czy.seed.mybatis.base.QueryParams">
        select distinct
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter)">
            <foreach collection="_parameter.selectColumns" item="selectColumn" separator=",">${name}.${r'${selectColumn}'}</foreach>
        </if>
        <if test="@com.czy.seed.mybatis.sql.util.MybatisColumnsOGNL@hasSelectColumns(_parameter) == false">
            <trim prefix=" " suffix=" " suffixOverrides=",">
            <#list entityClassColumns as column>
            ${name}.${column.column},
            </#list>
            <#list mybatisAssociations as association>
                <#list association.resultMap as result>
                ${association.targetTableName}_${association.property}.${result.column} as ${association.property}_${result.column},
                </#list>
            </#list>
            <#list mybatisConnections as connection>
                <#list connection.resultMap as result>
                ${connection.targetTableName}_${connection.property}.${result.column} as ${connection.property}_${result.column},
                </#list>
            </#list>
            </trim>
        </if>
        from ${name}
    <#list mybatisAssociations as association>
    ${association.joinCondition}
    </#list>
    <#list mybatisConnections as connection>
    ${connection.joinCondition}
    </#list>
        <if test="_parameter != null">
            <where>
                <foreach collection="orCriteria" item="criteria" separator="or">
                    <include refid="criteriaIsValid"/>
                </foreach>
            </where>
        </if>
    </select>

    <select id="selectCountByParams" resultType="int" parameterType="com.czy.seed.mybatis.base.QueryParams">
        select
        count(*)
        from ${name}
        <if test="_parameter != null">
            <where>
                <foreach collection="orCriteria" item="criteria" separator="or">
                    <include refid="criteriaIsValid"/>
                </foreach>
            </where>
        </if>
    </select>

    <!--没有查询条件时，查询条件解析-->
    <sql id="criteriaIsNotValid">
        <if test="criteria.valid == false">
            1 = -1
        </if>
    </sql>

    <!--有查询条件时，查询条件解析-->
    <sql id="criteriaIsValid">
        <if test="criteria.valid">
            <trim prefix="(" prefixOverrides="and" suffix=")">
                <foreach collection="criteria.criteria" item="criterion">
                    <choose>
                        <when test="criterion.noValue">
                            and ${r'${criterion.condition}'}
                        </when>
                        <when test="criterion.singleValue">
                            and ${name}.${r'${criterion.condition} #{criterion.value}'}
                        </when>
                        <when test="criterion.betweenValue">
                            and ${name}.${r'${criterion.condition} #{criterion.value} and #{criterion.secondValue}'}
                        </when>
                        <when test="criterion.listValue">
                            and ${name}.${r'${criterion.condition}'}
                            <foreach close=")" collection="criterion.value" item="listItem" open="(" separator=",">
                            ${r'#{listItem}'}
                            </foreach>
                        </when>
                    </choose>
                </foreach>
            </trim>
        </if>
    </sql>

</mapper>