# !/usr/bin/python3
# -*- coding: UTF-8 -*-


from selenium import webdriver
zhihulist = []

#
# def save():
#     print("save()")
#     #db = pymysql.connect('127.0.0.1:3334', 'root', '', 'little_see')
#     conn = mysql.connector.connect(user='root', password='', database='little_see')
#
#     # sql = "INSERT INTO zhihu (`id`, `titile`, `address`, `image_address`) VALUES ('', '2', '2', '2')"
#     # cursor.execute(sql)
#     # db.commit()

def main():
    #driver = webdriver.PhantomJS(executable_path="/Users/haizhi/Desktop/MyPythonShell/phantomjs-2.1.1-macosx/bin/phantomjs")
    #driver = webdriver.PhantomJS(executable_path="/Users/haizhi/Desktop/Little_See/Little_See/Little_See_Server/phantomjs-2.1.1-macosx/bin/phantomjs")
    driver = webdriver.PhantomJS(executable_path="/Users/yszsyf/Desktop/android/Little_See/Little_See_Server/phantomjs-2.1.1-macosx/bin/phantomjs")
    driver.get("http://daily.zhihu.com/")
    #print(driver.page_source)
    storelist = driver.find_elements_by_class_name("wrap")
    linklist = driver.find_elements_by_class_name("link-button")
    imagelist = driver.find_elements_by_class_name("preview-image")

    zhihu_length = len(storelist)

    for index  in range(zhihu_length):
        zhihu = []
        zhihu.insert(0, storelist[index].text)
        zhihu.insert(1, linklist[index].get_attribute("href"))
        zhihu.insert(2, imagelist[index].get_attribute("src"))
        zhihulist.append(zhihu)

    for zhihu in zhihulist:
        print(zhihu)

    # save()





main()