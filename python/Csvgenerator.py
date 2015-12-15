import json
import unicodecsv as csv
import re
import collections
import os

path = os.path.dirname(__file__)
dataFolderPath = os.path.join(path, "data")

with open(os.path.join(dataFolderPath, "combinedOutput.json")) as f:
    with open(os.path.join(dataFolderPath, "finalOutputWithOnly2Versions.csv"), "w") as wr:
        c = csv.writer(wr)
        giantDictionary = json.load(f)
        headerListWithFeaturePruning = ['BugId', 'Platform', 'JDT', 'CDT', 'PDE', 'blocker',
                                        'critical', 'major',
                                        'normal', 'minor', 'trivial', 'enhancement', 'REOPENED',
                                        'UI', 'Core', 'SWT', 'Debug', 'Text', 'Team', 'Resources',
                                        'cdt-core', 'User Assistance', 'Ant', 'CVS',
                                        'Update  (deprecated - use RT>Equinox>p2)',
                                        'Runtime', 'Build', 'IDE', 'Releng', 'deprecated7', 'cdt-parser',
                                        'cdt-debug', 'Compare', 'cdt-build', 'Doc', 'Search',
                                        'None', 'P1', 'P2', 'P3', 'P4', 'P5', 'Version', 'All', 'Mac',
                                        'Windows', 'Linux', 'other', 'resolution-time', 'FIXED']
        print len(headerListWithFeaturePruning)
        c.writerow(headerListWithFeaturePruning)
        productList = ['Platform', 'JDT', 'CDT', 'PDE']
        componentList = ['UI', 'Core', 'SWT', 'Debug', 'Text', 'Team', 'Resources',
                         'cdt-core', 'User Assistance', 'Ant', 'CVS',
                         'Update  (deprecated - use RT>Equinox>p2)',
                         'Runtime', 'Build', 'IDE', 'Releng', 'deprecated7', 'cdt-parser',
                         'cdt-debug', 'Compare', 'cdt-build', 'Doc', 'Search']
        severityList = ['blocker', 'critical', 'major',
                        'normal', 'minor', 'trivial', 'enhancement']
        priorityList = ['P1', 'P2', 'P3', 'P4', 'P5']
        versionList = ['2.0', '2.0.0', '2.0.1', '2.0.2', '2.0.3']
        osList = ['All', 'Mac', 'Windows', 'Linux', 'other']
        osCheckList = ['Mac', 'All', 'Windows', 'Linux']

        blockedSeverityList = []

        blockedOsList = []
        blockedComponentList = []
        # blockedPriorityList = ['None']

        cnt = collections.Counter()
        cnt2 = collections.Counter()
        cnt3 = collections.Counter()

        orderedListOfKeys = list()
        for key, value in giantDictionary.iteritems():
            orderedListOfKeys.append(int(key))

        orderedListOfKeys.sort()

        for key in orderedListOfKeys:
            key = str(key)
            value = giantDictionary[key]
            throwAway = False
            versionAdded = False
            for each in value['version']:
                cnt3[each['what']] += 1
            for severity in blockedSeverityList:
                if value['severity'][0]['what'] == severity:
                    throwAway = True
                    break
            for component in blockedComponentList:
                if value['component'][0]['what'] == component:
                    throwAway = True
                    break
            # for priority in blockedPriorityList:
            #     if value['priority'][0]['what'] == priority:
            #         continue
            #for each in value['version']:
            #    if each['what'] not in versionList:
            #        throwAway = True
            if throwAway:
                continue

            cnt[value['reports']['current_resolution']] += 1
            cnt2[value['reports']['current_status']] += 1
            row = list()
            row.append(key)
            for product in productList:
                row.append(1 if value['product'][0]['what'] == product else 0)
            for severity in severityList:
                row.append(1 if value['severity'][0]['what'] == severity else 0)
            count = 0
            for each in value['bug_status']:
                if each['what'] == 'REOPENED':
                    count += 1
            row.append(count)
            # x = re.sub('\n', '', value['short_desc'][0]['what'])
            # row.append(x)
            for component in componentList:
                row.append(1 if value['component'][0]['what'] == component else 0)
            row.append(1 if value['priority'][0]['what'] is None else 0)
            for priority in priorityList:
                row.append(1 if value['priority'][0]['what'] == priority else 0)
            # for version in versionList:
            #     row.append(1)
            #     break
            row.append(value['version'][0]['what'])
            row.append(1 if value['op_sys'][0]['what'] == 'All' else 0)
            row.append(1 if 'Mac' in value['op_sys'][0]['what'] else 0)
            row.append(1 if 'Windows' in value['op_sys'][0]['what'] else 0)
            row.append(1 if 'Linux' in value['op_sys'][0]['what'] else 0)
            row.append(1 if value['op_sys'][0]['what'] not in osCheckList else 0)
            start_time = value['resolution'][0]['when']
            end_time = value['resolution'][len(value['resolution'])-1]['when']
            row.append(end_time - start_time)
            if value['reports']['current_status'] == 'CLOSED' or value['reports']['current_status'] == 'VERIFIED':
                row.append(1)
            else:
                row.append(0)
            c.writerow(row)
            if len(row) != 50:
                print len(row)
        print cnt
        print cnt2
        print cnt3
