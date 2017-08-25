# crawl used car website and perform some basic ETL.
#
# append data: python car.py >> carsOut.txt


import requests
from bs4 import BeautifulSoup


def car_spider(max_pages):
	page = 1
	while page <= max_pages:
		url="https://www.cargurus.com/Cars/inventorylisting/viewDetailsFilterViewInventoryListing.action?#resultsPage=" + str(page)
		source_code = requests.get(url)
		plain_text = source_code.text
		soup = BeautifulSoup(plain_text)
		for car in soup.find_all("div", {"class": "cg-dealFinder-result-wrap clearfix"}):
			print car.contents[3].find_all("span", {"itemprop": "name"})[0].text[0:4].upper() \
			+ "|" \
			+ car.contents[3].find_all("span", {"itemprop": "name"})[0].text[4:].strip().upper() \
			+ "|" \
			+ car.contents[9].find_all("span", {"itemprop": "price"})[0].text.replace('Price','').replace('$','').replace(',','') \
			+ "|" \
			+ car.contents[9].find_all("p")[1].text.replace('Mileage:','').replace(',','').strip() \
			+ "|" \
			+ car.contents[9].find_all("p")[2].text[:-5].replace('Location:','').replace(', ','|').strip().upper()
		page += 1

car_spider(1)