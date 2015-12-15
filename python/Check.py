import json
priority_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/priority.json', 'r')
data = json.load(priority_file)
priority_data = data["priority"]
priority_file.close()

key1 = list()

for key, value in priority_data.iteritems():
    key1.append(key)

op_sys_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/op_sys.json', 'r')
data2 = json.load(op_sys_file)
op_sys_data = data2['op_sys']
op_sys_file.close()

key2 = list()

for key, value in op_sys_data.iteritems():
    key2.append(key)

assigned_to_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/assigned_to'
                            '.json', 'r')
data3 = json.load(assigned_to_file)
assigned_to_data = data3['assigned_to']
assigned_to_file.close()

key3 = list()

for key, value in assigned_to_data.iteritems():
    key3.append(key)

# bug_status_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/bug_status.json', 'r')
# data4 = json.load(bug_status_file)
# bug_status_data = data4['bug_status']
# bug_status_file.close()
#
# key4 = list()
#
# for key, value in bug_status_data.iteritems():
#     key4.append(key)
#
# cc_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/cc.json', 'r')
# data5 = json.load(cc_file)
# cc_data = data5['cc']
# cc_file.close()
#
# key5 = list()
#
# for key, value in cc_data.iteritems():
#     key5.append(key)
#
# component_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/component.json', 'r')
# data6 = json.load(component_file)
# component_data = data6["component"]
# component_file.close()
#
# key6 = list()
#
# for key, value in component_data.iteritems():
#     key6.append(key)
#
# product_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/product.json', 'r')
# data7 = json.load(product_file)
# product_data = data7['product']
# product_file.close()
# key7 = list()
#
# for key, value in product_data.iteritems():
#     key7.append(key)
#
# reports_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/reports.json', 'r')
# data8 = json.load(reports_file)
# reports_data = data8['reports']
# reports_file.close()
# key8 = list()
#
# for key, value in reports_data.iteritems():
#     key8.append(key)
#
# resolution_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/resolution.json', 'r')
# data9 = json.load(resolution_file)
# resolution_data = data9['resolution']
# resolution_file.close()
# key9 = list()
#
# for key, value in resolution_data.iteritems():
#     key9.append(key)
#
# severity_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/severity.json', 'r')
# data10 = json.load(severity_file)
# severity_data = data10['severity']
# severity_file.close()
# key10 = list()
#
# for key, value in severity_data.iteritems():
#     key10.append(key)
#
# short_desc_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/short_desc.json', 'r')
# data11 = json.load(short_desc_file)
# short_desc_data = data11['short_desc']
# short_desc_file.close()
# key11 = list()
#
# for key, value in short_desc_data.iteritems():
#     key11.append(key)

version_file = open('/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/version.json', 'r')
data12 = json.load(version_file)
version_data = data12['version']
version_file.close()
key12 = list()

for key, value in version_data.iteritems():
    key12.append(int(key))
key12.sort()

result = set(key1).difference(key2)
print len(result)
print len(key1)
print len(key2)
print len(key3)
with open('/home/naineel/keyss.txt', 'w') as w:
    for each in key12:
        w.write(str(each) + "\n")
