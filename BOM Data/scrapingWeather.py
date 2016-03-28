#program requires BeautifulSoup and python requests to be installed

import requests
from bs4 import BeautifulSoup

#states = {"vic", "nsw", "tas", "wa", "sa", "nt", "qld", "ant"}

urlVIC = "http://www.bom.gov.au/vic/observations/vicall.shtml"
urlNSW = "http://www.bom.gov.au/nsw/observations/nswall.shtml"
urlTAS = "http://www.bom.gov.au/tas/observations/tasall.shtml"
urlWA = "http://www.bom.gov.au/wa/observations/waall.shtml"
urlSA = "http://www.bom.gov.au/sa/observations/saall.shtml"
urlNT = "http://www.bom.gov.au/nt/observations/ntall.shtml"
urlQLD ="http://www.bom.gov.au/qld/observations/qldall.shtml"
urlANT = "http://www.bom.gov.au/ant/observations/antall.shtml"


r = requests.get(urlVIC)
soup = BeautifulSoup(r.content)
print '[\n'
print '%5s\n%29s\n%20s' %('{', '"state": "Victoria",', '"stations": [')	
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')



r = requests.get(urlNSW)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "New South Wales",', '"stations": [')
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')



r = requests.get(urlTAS)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "Tasmania",', '"stations": [')
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')


r = requests.get(urlWA)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "Western Australia",', '"stations": [')
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')


r = requests.get(urlSA)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "South Australia",', '"stations": [')
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')


r = requests.get(urlNT)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "Northern Territory",', '"stations": [')

for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')


r = requests.get(urlQLD)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "Queensland",', '"stations": [')
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')


r = requests.get(urlANT)
soup = BeautifulSoup(r.content)
print '%4s\n%34s\n%20s' %('{', '"state": "Antartica",', '"stations": [')
for table in soup.findAll('table'):
	links=table.findAll('a')
	for link in links:
		print '%15s' %('{')
		print '%25s: %s\n%24s: www.bom.gov.au/fwo%s.json' %('"city"', link.text, '"url"', link.get("href")[9:-6])
		print '%16s' %('},')
print '%10s\n%5s' %(']', '},')
	
print ']'
