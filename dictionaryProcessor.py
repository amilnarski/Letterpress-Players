import re
#open the files
f = open ('/Users/aaron/Desktop/dictionary.txt','r')
processedDict = open ('/Users/aaron/Desktop/pDictionary.txt','a')

#define the regex pattern for matching lines

#loop over all lines of the file
lastMatch = ''
for each in f:
	#if there's a match, write the matched text to the file
	line = f.next()
	#matchObj = re.match(r'^[A-Z]{2,}[^-*\'\.a-z0-9]?\b',line)
	matchObj = re.match(r'^[A-Z]{2,}\s?\n',line)
	if (matchObj):
		if (matchObj.group() != lastMatch):
			processedDict.write(matchObj.group())
			#processedDict.write('\n')
			lastMatch = matchObj.group()

#close the files
f.close()
processedDict.close()

print 'FINISHED'
