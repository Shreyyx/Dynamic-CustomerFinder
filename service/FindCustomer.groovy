import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityList
import org.moqui.entity.EntityValue

ExecutionContext ec = context.ec
EntityFind ef = ec.entity.find("mantle.party.FindCustomerView").distinct(true)

ef.selectField("partyId")

if (partyId) { ef.condition(ec.entity.conditionFactory.makeCondition("partyId", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + partyId + "%").ignoreCase()) }
if (partyTypeEnumId) { ef.condition("partyTypeEnumId", partyTypeEnumId) }

if (combinedName) {
    String fnSplit = combinedName
    String lnSplit = combinedName
    if (combinedName.contains(" ")) {
        fnSplit = combinedName.substring(0, combinedName.indexOf(" "))
        lnSplit = combinedName.substring(combinedName.indexOf(" ") + 1)
    }
    cnCondList = [ec.entity.conditionFactory.makeCondition("organizationName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + combinedName + "%").ignoreCase(),
                  ec.entity.conditionFactory.makeCondition("firstName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + fnSplit + "%").ignoreCase(),
                  ec.entity.conditionFactory.makeCondition("lastName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + lnSplit + "%").ignoreCase()]
    ef.condition(ec.entity.conditionFactory.makeCondition(cnCondList, EntityCondition.OR))
}

if (organizationName) { ef.condition(ec.entity.conditionFactory.makeCondition("organizationName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + organizationName + "%").ignoreCase()) }
if (firstName) { ef.condition(ec.entity.conditionFactory.makeCondition("firstName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + firstName + "%").ignoreCase()) }
if (lastName) { ef.condition(ec.entity.conditionFactory.makeCondition("lastName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + lastName + "%").ignoreCase()) }

if (address1) { ef.condition(ec.entity.conditionFactory.makeCondition("address1", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + address1 + "%").ignoreCase()) }
if (address2) { ef.condition(ec.entity.conditionFactory.makeCondition("address2", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + address2 + "%").ignoreCase()) }
if (city) { ef.condition(ec.entity.conditionFactory.makeCondition("city", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + city + "%").ignoreCase()) }
if (postalCode) { ef.condition(ec.entity.conditionFactory.makeCondition("postalCode", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + postalCode + "%").ignoreCase()) }
if (contactNumber) { ef.condition(ec.entity.conditionFactory.makeCondition("contactNumber", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + contactNumber + "%")) }
if (emailAddress) { ef.condition(ec.entity.conditionFactory.makeCondition("emailAddress", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + emailAddress + "%").ignoreCase()) }

if (orderByField) {
    if (orderByField.contains("combinedName")) {
        if (orderByField.contains("-")) ef.orderBy("-organizationName,-firstName,-lastName")
        else ef.orderBy("organizationName,firstName,lastName")
    } else {
        ef.orderBy(orderByField)
    }
}

if (!pageNoLimit) { ef.offset(pageIndex as int, pageSize as int); ef.limit(pageSize as int) }

partyIdList = []
EntityList el = ef.list()
for (EntityValue ev in el) partyIdList.add(ev.partyId)

partyIdListCount = ef.count()
partyIdListPageIndex = ef.pageIndex
partyIdListPageSize = ef.pageSize
partyIdListPageMaxIndex = ((BigDecimal) (partyIdListCount - 1)).divide(partyIdListPageSize, 0, BigDecimal.ROUND_DOWN) as int
partyIdListPageRangeLow = partyIdListPageIndex * partyIdListPageSize + 1
partyIdListPageRangeHigh = (partyIdListPageIndex * partyIdListPageSize) + partyIdListPageSize
if (partyIdListPageRangeHigh > partyIdListCount) partyIdListPageRangeHigh = partyIdListCount