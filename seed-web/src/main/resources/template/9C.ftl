CQH LTS </br>
${header}</br>
${flightInfo.flightNo} <#list manifest.manySegmentList as manySegment>
&nbsp;/&nbsp;${manySegment.segmentName}
</#list>
</br>
${cockpitActuralNum}-${midActuralNum}-${serviceActuralNum}/${fwdActualWeight}-${aftActualWeight}/${flightInfo.flightConfig.takeOffFuel}-${flightInfo.flightConfig.tripFuel} </br>
${flightInfo.flightConfig.msn}-${flightInfo.flightConfig.baseWeight}-${flightInfo.flightConfig.baseIndex}-${flightInfo.flightConfig.limitedMtow}-${flightInfo.flightConfig.limitedMldw}-${flightInfo.flightConfig.limitedMzfw}-${flightInfo.flightTypeConfig.seatNum}</br>
<#list manifest.manySegmentList as manySegment>
    <#list manySegment.cabinList as cabin>
        <#if cabin_index==0>
        Cabin:&nbsp;
        </#if>
        <#list cabin.passengerList as passenger>
            <#if cabin_index==0 >
            ${passenger.name}
            </#if>
            <#if (cabin_index==0 &&(cabin.passengerList?size > (passenger_index+1)))>
            -
            </#if>
        </#list>
        <#if cabin_index==0>
        </br>&nbsp;${manySegment.segmentName}</br>
        </#if>
        ${cabin.type?substring(0,1)}:&nbsp;
        <#list cabin.passengerList as passenger>
        ${passenger.num}&nbsp;
            <#if (cabin.passengerList?size > (passenger_index+1))>
            -
            </#if>
        </#list>
    </br>
    </#list>

</#list>
<#list manifest.manySegmentList as manySegment>
    <#list manySegment.cargoList as cargo>
        <#if cargo_index==0>
        Cargo:&nbsp;
        </#if>
        <#list cargo.cargoHoldList as cargoHold>
            <#if cargo_index==0>
            ${cargoHold.name}
            </#if>
            <#if (cargo_index==0 &&(cargo.cargoHoldList?size > (cargoHold_index+1)))>
            -
            </#if>
        </#list>
        <#if cargo_index==0>
        /fs
        </#if>
        <#if ((manySegment.cargoList?size>cargo_index) && (cargo_index==0))>
        </br>&nbsp;${manySegment.segmentName}</br>
        </#if>
    ${cargo.type?substring(0,1)}:&nbsp;
        <#list cargo.cargoHoldList as cargoHold>
        ${cargoHold.weight}&nbsp;
            <#if (cargo.cargoHoldList?size > (cargoHold_index+1))>
            -
            </#if>
        </#list>
    /&nbsp;${cargo.fullScale}
    </br>
    </#list>
</#list>

LtdPL=  ${plMtow}/TO  ${plMldw}/LD    ${plMzfw}/ZF    </br>
PL_Usb= ${minPlNum}    Act=   ${payload} Spa= ${spa}  </br>
DO  :   ${ldMath.dow}/${ldMath.doi}</br>
ZF  :   ${ldMath.zfw}/${ldMath.zfi}/${ldMath.zfCg}% </br>
TO  :   ${ldMath.tow}/${ldMath.toi}/${ldMath.toCg}%/${ldMath.trimToCg}  <br>
LD  :   ${ldMath.ldw}/${ldMath.ldi}/${ldMath.ldCg}%/${ldMath.trimCg}     <br>
P   :   ${adultNum}-${childNum}-${infantNum}  </br>
C   :   ${cWeight}/${cFs} </br>
B   :   ${bWeight}/${bFs}   </br>
M   :   ${mWeight}/${mFs}  </br>
Pre     :   ${pre}  </br>
App     :   ${app}  </br>
Sig     :   ${sig}  </br>
${nowDate?string("yyyy-MM-dd HH:mm:ss")}

