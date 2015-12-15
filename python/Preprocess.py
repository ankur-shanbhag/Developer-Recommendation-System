import json
import os

path = os.path.dirname(__file__)
dataFolderPath = os.path.join(path, "data")

priority_file = open(os.path.join(dataFolderPath, 'priority.json'), 'r')
data = json.load(priority_file)
priority_data = data["priority"]
priority_file.close()

op_sys_file = open(os.path.join(dataFolderPath, 'op_sys.json'), 'r')
data2 = json.load(op_sys_file)
op_sys_data = data2['op_sys']
op_sys_file.close()

assigned_to_file = open(os.path.join(dataFolderPath, 'assigned_to.json'), 'r')
data3 = json.load(assigned_to_file)
assigned_to_data = data3['assigned_to']
assigned_to_file.close()

bug_status_file = open(os.path.join(dataFolderPath, 'bug_status.json'), 'r')
data4 = json.load(bug_status_file)
bug_status_data = data4['bug_status']
bug_status_file.close()

cc_file = open(os.path.join(dataFolderPath, 'cc.json'), 'r')
data5 = json.load(cc_file)
cc_data = data5['cc']
cc_file.close()

component_file = open(os.path.join(dataFolderPath, 'component.json'), 'r')
data6 = json.load(component_file)
component_data = data6["component"]
component_file.close()

product_file = open(os.path.join(dataFolderPath, 'product.json'), 'r')
data7 = json.load(product_file)
product_data = data7['product']
product_file.close()

reports_file = open(os.path.join(dataFolderPath, 'reports.json'), 'r')
data8 = json.load(reports_file)
reports_data = data8['reports']
reports_file.close()

resolution_file = open(os.path.join(dataFolderPath, 'resolution.json'), 'r')
data9 = json.load(resolution_file)
resolution_data = data9['resolution']
resolution_file.close()

severity_file = open(os.path.join(dataFolderPath, 'severity.json'), 'r')
data10 = json.load(severity_file)
severity_data = data10['severity']
severity_file.close()

short_desc_file = open(os.path.join(dataFolderPath, 'short_desc.json'), 'r')
data11 = json.load(short_desc_file)
short_desc_data = data11['short_desc']
short_desc_file.close()

version_file = open(os.path.join(dataFolderPath, 'version.json'), 'r')
data12 = json.load(version_file)
version_data = data12['version']
version_file.close()

uniqueIds = []
giantDictionary = {}
for key, value in priority_data.items():
    uniqueIds.append(int(key))
uniqueIds.sort()

for key in uniqueIds:
    smallDict = {}
    keyData = priority_data[str(key)]
    smallDict['priority'] = priority_data[str(key)]
    # keyData.append(op_sys_data[str(key)])
    smallDict['op_sys'] = op_sys_data[str(key)]
    # keyData.append(assigned_to_data[str(key)])
    smallDict['assigned_to'] = assigned_to_data[str(key)]
    # keyData.append(bug_status_data[str(key)])
    smallDict['bug_status'] = bug_status_data[str(key)]
    # keyData.append(cc_data[str(key)])
    smallDict['cc'] = cc_data[str(key)]
    # keyData.append(component_data[str(key)])
    smallDict['component'] = component_data[str(key)]
    # keyData.append(product_data[str(key)])
    smallDict['product'] = product_data[str(key)]
    # keyData.append(reports_data[str(key)])
    smallDict['reports'] = reports_data[str(key)]
    # keyData.append(resolution_data[str(key)])
    smallDict['resolution'] = resolution_data[str(key)]
    # keyData.append(severity_data[str(key)])
    smallDict['severity'] = severity_data[str(key)]
    # keyData.append(short_desc_data[str(key)])
    smallDict['short_desc'] = short_desc_data[str(key)]
    # keyData.append(version_data[str(key)])
    smallDict['version'] = version_data[str(key)]
    giantDictionary[key] = smallDict

with open(os.path.join(dataFolderPath, 'combinedOutput.json'), 'w') as output:
    json.dump(giantDictionary, output, encoding="utf-8")
