import json
import collections
import os

path = os.path.dirname(__file__)
dataFolderPath = os.path.join(path, "data")
with open(os.path.join(dataFolderPath, "combinedOutput.json")) as f:
    giantDictionary = json.load(f)
    developers = list()
    ccDevelopers = list()

    cntWithoutEclipseEmails = collections.Counter()
    cntWithEclipseEmails = collections.Counter()
    ccCountWithoutEclipseEmails = collections.Counter()
    ccCountWithEclipseEmails = collections.Counter()

    for key, value in giantDictionary.iteritems():
        listOfDev = value['assigned_to']
        listOfCc = value['cc']
        for dev in listOfDev:
            developers.append(dev)
        for ccdev in listOfCc:
            ccDevelopers.append(ccdev)

    lNames = [None, u'Platform-UI-Inbox@eclipse.org', u'platform-ui-triaged@eclipse.org',
              u'wst.sse-triaged@eclipse.org',
              u'eclipse.org-architecture-council@eclipse.org', u'swt-triaged@eclipse.org',
              u'jdt-core-triaged@eclipse.org', u'PDE-Incubator-Inbox@eclipse.org',
              u'CDT-Contrib-Inbox@eclipse.org', u'webmaster@eclipse.org',
              u'Platform-UI-Unassigned@eclipse.org', u'unknown1@eclipse.org',
              u'wtp-dev@eclipse.org', u'mylyn-triaged@eclipse.org', u'emo@eclipse.org']

    actualNames = list()
    for developer in developers:
        if developer['what'] not in lNames and 'inbox@eclipse.org' not in developer['what']:
            cntWithoutEclipseEmails[developer['what']] += 1

    for developer in developers:
        cntWithEclipseEmails[developer['what']] += 1

    for ccDev in ccDevelopers:
        ccCountWithEclipseEmails[ccDev['what']] += 1

    for ccDev in ccDevelopers:
        if ccDev['what'] not in lNames and 'inbox@eclipse.org' not in ccDev['what']:
            ccCountWithoutEclipseEmails[ccDev['what']] += 1

    with open('/home/naineel/countOfDevelopersWithoutEclipseEmails.csv', 'w') as fi:
        fi.write('assigned_to_email' + ',' + 'AssignedToCount' + ',' + 'ccCount' + '\n')
        fi.write('Number of Assigned to items: ' + str(len(cntWithoutEclipseEmails)) + '\n')
        fi.write('Number of cc items: ' + str(len(ccCountWithoutEclipseEmails)) + '\n')
        for key in cntWithoutEclipseEmails.iterkeys():
            fi.write(str(key) + ',' + str(cntWithEclipseEmails[key]) + ',' + str(
                ccCountWithoutEclipseEmails[key]) + '\n')
        fi.close()

    with open('/home/naineel/countOfDevelopers.csv', 'w') as ri:
        ri.write('assigned_to_email' + ',' + 'AssignedToCount' + ',' + 'ccCount' + '\n')
        ri.write('Number of Assigned to items: ' + str(len(cntWithEclipseEmails)) + '\n')
        ri.write('Number of cc items: ' + str(len(ccCountWithEclipseEmails)) + '\n')
        for key in cntWithEclipseEmails.iterkeys():
            ri.write(str(key) + ',' + str(cntWithEclipseEmails[key]) + ',' + str(
                ccCountWithEclipseEmails[key]) + '\n')
        ri.close()
