import json
import unicodecsv as csv

with open("/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/combinedOutput.json") as f:
    with open("/home/naineel/eclipse-dataset/msr2013-bug_dataset/data/bugToDeveloperMapping.csv",
              "w") as wr:
        giantDictionary = json.load(f)
        wr.write('BugId' + ',' + 'Developer')
        for key, value in giantDictionary.iteritems():
            developers = list()
            listOfDev = value['assigned_to']
            developers.append(key)
            for each in listOfDev:
                developers.append(each['what'])
            # print developers
            for one in developers:
                # print one
                if one is None:
                    wr.write(' ,')
                else:
                    wr.write(one + ',')
            wr.write('\n')

