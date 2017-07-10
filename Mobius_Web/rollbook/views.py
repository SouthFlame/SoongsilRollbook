#-*- coding:utf-8 -*-
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt

from .models import *

import json
from django.http import HttpResponse
from django.http import JsonResponse

import time
import datetime
import random





def pro_fetchCode():
    rows = Professor.objects.all()

    max = 0
    for i in rows:
        if i.pro_id > max:
            max = i.pro_id

    return max+1

def lec_fetchCode():
    rows = Lecture.objects.all()

    max = 0
    for i in rows:
        if i.lec_id > max:
            max = i.lec_id

    return max+1

def stu_fetchCode():
    rows = Student.objects.all()

    max = 0
    for i in rows:
        if i.stu_id > max:
            max = i.stu_id

    return max+1

def stuInLec_fetchCode():
    rows = StuInLec.objects.all()

    max = 0
    for i in rows:
        if i.sil_id > max:
            max = i.sil_id

    return max+1



# Create your views here.
def student_list(request):
    students = Student.objects.all()
    return render(request, 'rollbook/student_list.html', {"students":students})


@csrf_exempt
def test(request):

    print('test들어옴')
    select = -1

    professor_ = ''
    student_   = ''
    text       = ''

    students = Student.objects.all()
    professors = Professor.objects.all()

    if request.method == 'POST':
        print('In Post')
        # students = Student.objects.all()
        msg = ""
        response_data = {}


        id_= request.POST['id']
        password_ = request.POST['pwd']

        print(id_)
        print(password_)


        try:
            select = 0
            professor_ = Professor.objects.get(pro_id=id_) # 교수님에 일치하는 id가 있는 경우, select = 0
        except:
            try:
                select = 1
                student_ = Student.objects.get(st_id=id_)  # 학생에 일치하는 id가 있는 경우, select = 1
            except:
                select = -1
                text = 'ID INCORRECT'                      # 교수, 학생 모두 일치하는 id가 없는 경우, select = -1

        if select == 0:
            if professor_.pro_pwd == password_:            # id가 교수에 일치 and pw 일치
                request.session['member_id'] = id_         # 세션처리
                print(request.session['member_id'])
                lectures = Lecture.objects.filter(lec_professor=professor_.pro_id)
                print("In 교수일치")
                # lectures = []
                # for item in test:
                #     lectures.append(item)
                print(lectures)
                text = 'ok'

                data = {'param':text, 'sign':'pro', 'user_id':professor_.pro_id, 'user_name':professor_.pro_name}

                lecture_data = []
                for lecture in lectures:
                    lec_record = {}
                    lec_record['lec_id'] = lecture.lec_id
                    lec_record['lec_name'] = lecture.lec_name
                    lec_record['lec_access'] = lecture.lec_access
                    lec_record['lec_room'] = lecture.lec_room
                    lec_record['lec_time'] = lecture.lec_time

                    lecture_data.append(lec_record)

                data['lecture'] = lecture_data


                return JsonResponse(data, content_type="application/json; charset=utf-8",)


                # return render(request, 'rollbook/test.html', {'param':text, 'sign':'pro', 'professor': professor_, 'lectures':lectures})
            else:
                text = 'PW INCORRECT'
                #return render(request, 'blog/login.html', {'param': 'PW INCORRECT', 'sign': 'no'})

        elif select == 1:
            if student_.st_pwd == password_:               # id가 학생에 일치 and pw 일치
                request.session['member_id'] = id_         # 세션처리
                stuinlecs = StuInLec.objects.filter(sil_stu=student_.st_id)
                stuLectures = []

                for i in stuinlecs:
                    lec = Lecture.objects.get(lec_id=i.sil_lec.lec_id)
                    stuLectures.append(lec)

                print(stuLectures)
                # for item in stuinlecs:
                #     sil = StuInLec.objects.filter(sil_lec=item.sil_stu)
                #     for inItem in sil:
                #         lec = Lecture.objects.get(inItem.sil_lec)
                #         stuLectures.append(sil)
                #

                text = 'ok'

                data = {'param':text, 'sign':'stu', 'user_id':student_.st_id, 'user_name':student_.st_name}

                lecture_data = []
                for lecture in stuLectures:
                    lec_record = {}
                    lec_record['lec_id'] = lecture.lec_id
                    lec_record['lec_name'] = lecture.lec_name
                    lec_record['lec_access'] = lecture.lec_access
                    lec_record['lec_room'] = lecture.lec_room
                    lec_record['lec_time'] = lecture.lec_time
                    lecture_data.append(lec_record)

                data['lecture'] = lecture_data

                return JsonResponse(data)
                # return render(request, 'rollbook/test.html', {'param':text, 'sign':'stu', 'student': student_, 'stuLectures':stuLectures})
                # return render(request, 'rollbook/test.html', data = json.dumps(data))

            else:
                text = 'PW INCORRECT'
                #return render(request, 'blog/login.html', {'param': 'PW INCORRECT', 'sign': 'no'})
        return render(request, 'rollbook/test.html', {'param':text, 'sign':'no'})



    return render(request, 'rollbook/test.html', {"students":students})


@csrf_exempt
def registerUser(request):
    print('In RegisterUser')
    if request.method == 'POST':
        print('In Post')
        id_ = request.POST['id']
        name_ = request.POST['name']
        pwd_ = request.POST['pwd']
        isPro_ = request.POST['position']
        print("id : " + id_ + "name : " + name_ + "pwd : " + pwd_ + "pos : " + isPro_)

        if str(isPro_) == "pro":
            print("In pro")
            Professor.objects.create(pro_id=id_, pro_pwd=pwd_, pro_name=name_, pro_isPro=True, pro_Beacon=123,
                                     pro_limTime=30, pro_startTime=1)
            print("added!")


            data = {'sign': 'pro', 'result':'ok'}

            return JsonResponse(data, content_type="application/json; charset=utf-8")


            # return render(request, 'rollbook/registerUser.html',
            #               {"professors": Professor.objects.all(), "sign": isPro_})
        else:
            print("In stu")
            Student.objects.create(st_id=id_, st_pwd=pwd_, st_name=name_, st_isPro=False)
            print("added!")
            data = {'sign': 'stu', 'result':'ok'}

            return JsonResponse(data, content_type="application/json; charset=utf-8")

        return render(request, 'rollbook/registerUser.html', {"students": Student.objects.all(), "sign": isPro_})

    return render(request, 'rollbook/registerUser.html', {"students": Student.objects.all(), "sign": 'no'})

@csrf_exempt
def start_attendanceCheck(request): #학생이 출석체크 하는 함수 lec_access 받아옴->교수찾음
    if request.method == 'POST':
        st_id_ = request.POST['st_id']
        motion_ = request.POST['motion']
        lec_access_ = request.POST['lec_access']
        ipAddr_ = request.POST.get('st_ipAddr', False)

        student = Student.objects.get(st_id=st_id_)
        student.st_ipAddr = ipAddr_
        student.save()
        lec = Lecture.objects.get(lec_access = lec_access_)

        pro = Professor.objects.get(pro_id=lec.lec_professor.pro_id)
        st_ip = ipAddr_
        pro_ip = pro.pro_ipAddr
        print('st_ip' + st_ip)
        print('pro_ip' + pro_ip)
        st_ip_arr = st_ip.split('.')
        pro_ip_arr = pro_ip.split('.')

        res2 = ""


        res = 'N'

        clickTime = int(time.time())
        lecTimeInterval = 4500

        # print('time : ' + (clickTime - pro.pro_startTime))

        if (clickTime - pro.pro_startTime) <= pro.pro_limTime :
            res = 'Y'
            print('출석!')
        elif(clickTime - pro.pro_startTime) <= lecTimeInterval :
            res = 'L'
            print('지각!')
        else :
            print('결석!')
            res = 'N'

        if motion_ != lec.lec_motion :
            res2 = 'Incorrect'
            print('불일치')

        for i in range(3):
            if st_ip_arr[i] != pro_ip_arr[i] :
                res2 = 'Out'
                print('범위밖')
                break

        if res2 == 'Incorrect' or res2 == 'Out':
            res = 'N'

        # print("datatime.now" + datetime.now())
        # print(datetime.date.today())
        try:
            adr = AttendanceRecord.objects.get(ad_lec=lec, ad_stu=student, ad_date=datetime.date.today())
            print("try")
            print(adr.ad_date)
        except:
            adr = None
            print("NONE")

        if adr == None:
            AttendanceRecord.objects.create(ad_state=res, ad_lec=lec,
                                            ad_stu=student)
        else:
            adr.ad_state=res
            adr.save()


        if res2 != "":
            res = res2



        print("clickTime : "+ str(clickTime))
        print("pro_StartTime : "+ str(pro.pro_startTime))


        data = {'result': res,'data':adr.ad_date}

        return JsonResponse(data, content_type="application/json; charset=utf-8")
    return render(request, 'rollbook/start_attendanceCheck.html', {})


@csrf_exempt
def start_timecnt(request): #교수가 출석체크 시작
    # print('In start_timecnt')

    if request.method == 'POST':
        # 시간설정

        print('In start_timecnt')
        limitsec_ = request.POST["limitsec"]
        id_ = request.POST["id"]
        access_=request.POST["access"]
        motion_=request.POST["motion"]
        ipAddr_ = request.POST.get('pro_ipAddr', False)
        print(motion_)
        # time_.split(':')
        # min = int(time_[0])
        # sec = int(time_[1])
        # limitsec = min * 60 + sec
        try:
            pro = Professor.objects.get(pro_id=id_)
        except:
            pro = None

        pro.pro_limTime = limitsec_
        pro.pro_startTime = int(time.time())
        pro.pro_ipAddr = ipAddr_
        pro.save()

        # 모션설정(l/r/u/d)

        lec=Lecture.objects.get(lec_access=access_)
        lec.lec_motion = motion_
        lec.save()

        data = {'result': 'ok'}

        return JsonResponse(data, content_type="application/json; charset=utf-8")
    # start_attendanceCheck(request, Professor.objects.get(pro_id=id_))
    # start_attendanceCheck(request,pro)
    return render(request, 'rollbook/start_timecnt.html', {})

@csrf_exempt
def pro_add_subject(request): #교수가 강의 추가하는 함수

    # pro_id로 교수찾고 lecture만들어서 lec_professor 에 교수넣어서 lecture db에 추가. (이 때 lec_access# 랜덤리 생성)

    random.seed()
    access_ = 0

    if request.method == 'POST':
        print('In Post')

        id_ = request.POST['id']
        lec_name_ = request.POST['lec_name']
        lec_room_= request.POST['lec_room']
        lec_time_ = request.POST['lec_time']
        # lec_motion_ =
        print('id : ' + id_ + "lec_name : " + lec_name_)
        x = False
        while x == False :
            access_ = random.randint(0, 99999999)
            try:
                x = False
                x = Lecture.objects.get(lec_access=access_)
            except Lecture.DoesNotExist:
                x = True
            # print('access : '+ access_)

        professor_ = Professor.objects.get(pro_id=id_)

        Lecture.objects.create(lec_professor=professor_, lec_name=lec_name_, lec_access=access_, lec_room=lec_room_, lec_time=lec_time_)
        print("added!")

        data = {'result':'ok'}
        lecture_ = Lecture.objects.get(lec_access=access_)
        lecture_data = []
        lec_record = {}
        lec_record['lec_professor'] = lecture_.lec_professor.pro_id
        lec_record['lec_id'] = lecture_.lec_id
        lec_record['lec_name'] = lecture_.lec_name
        lec_record['lec_access'] = lecture_.lec_access
        lec_record['lec_room'] = lecture_.lec_room
        lec_record['lec_time'] = lecture_.lec_time
        # lec_record['lec_motion'] = lecture_.lec_motion
        lecture_data.append(lec_record)

        data['lecture'] = lecture_data

        return JsonResponse(data, content_type="application/json; charset=utf-8")

    return render(request, 'rollbook/pro_add_subject.html', {"lecture": Lecture.objects.get(lec_access=access_)})


@csrf_exempt
def stu_add_subject(request): #학생이 강의에 등록하는 함수

    #교수가 access code 주면 그거 입력한 후 stuinlec에 추가, ok,no 리턴
    if request.method == 'POST':
        print('In Post')

        id_=request.POST['id']
        access_ = request.POST['access']
        try:
            lecture_ = Lecture.objects.get(lec_access = access_)
        except:
            lecture_ = None

        try:
            student_ = Student.objects.get(st_id = id_)
        except:
            student_ = None

        if(lecture_ != None and student_ != None):
            StuInLec.objects.create(sil_lec=lecture_, sil_stu=student_)
            print("added!")

            data = {'result': 'ok'}

            lecture_data = []
            lec_record = {}
            lec_record['lec_professor'] = lecture_.lec_professor.pro_id
            lec_record['lec_id'] = lecture_.lec_id
            lec_record['lec_name'] = lecture_.lec_name
            lec_record['lec_access'] = lecture_.lec_access
            lec_record['lec_room'] = lecture_.lec_room
            lec_record['lec_time'] = lecture_.lec_time
            # lec_record['lec_motion'] = lecture_.lec_motion
            lecture_data.append(lec_record)

            data['lecture'] = lecture_data

        else:
            print("Failed!")
            data = {'sign':'no'}

        return JsonResponse(data, content_type="application/json; charset=utf-8")


    return render(request, 'rollbook/stu_add_subject.html',{})

#---------------------------------------------------시작이다----------------------------------------------------

@csrf_exempt
def login(request):                                       # 로그인 처리 함수
    select = -1

    professor_ = ''
    student_   = ''
    text       = ''

    if request.method == 'POST':                           # POST방식 일 경우
        id_       = request.POST['id']                     # 입력한 id를 받아온다.
        password_ = request.POST['pw']                     # 입력한 pw를 받아온다.

        try:
            select = 0
            professor_ = Professor.objects.get(pro_id=id_) # 교수님에 일치하는 id가 있는 경우, select = 0
        except:
            try:
                select = 1
                student_ = Student.objects.get(st_id=id_)  # 학생에 일치하는 id가 있는 경우, select = 1
            except:
                select = -1
                text = 'ID INCORRECT'                      # 교수, 학생 모두 일치하는 id가 없는 경우, select = -1

        if select == 0:
            if professor_.pro_pwd == password_:            # id가 교수에 일치 and pw 일치
                text = 'soongsil note'
                return render(request, 'rollbook/web_Login_Form.html', {'param':text, 'sign':'pro', 'pro_id': professor_.pro_id})       #html에 sign과 pro_id값을 넣어준다.
            else:
                text = 'PW INCORRECT'
                #return render(request, 'rollbook/login.html', {'param': 'PW INCORRECT', 'sign': 'no'})

        elif select == 1:
            if student_.st_pwd == password_:               # id가 학생에 일치 and pw 일치
                text = 'soongsil note'
                return render(request, 'rollbook/web_Login_Form.html', {'param':text, 'sign':'stu', 'st_id': student_.st_id})
            else:
                text = 'PW INCORRECT'
                #return render(request, 'rollbook/login.html', {'param': 'PW INCORRECT', 'sign': 'no'})
        return render(request, 'rollbook/web_Login_Form.html', {'param':text, 'sign':'no'})

    return render(request, 'rollbook/web_Login_Form.html', {'param': 'Soongsil Rollbook', 'sign':'no'})

@csrf_exempt
def login_find_pw(request):                    # 비밀번호 찾는 함수
    id     = 0;  id_who    = "stu"             # id와 id_who 초기화
    name   = 0;  name_who  = "stu"             # id와 id_who 초기화
    beacon  = 0;  beacon_who = "stu"             # id와 id_who 초기화
    pw     = "확인"

    if request.method == 'POST':               # POST방식 인 경우
        id_       = request.POST['id']         # 입력 한 id를 받는 다
        print(id_)
        name_     = request.POST['name']       # 입력 한 name를 받는 다
        print(name_)
        beacon_   = request.POST['beacon']      # 입력 한  Beacon를 받는 다
        print(beacon_)


        # try:                                                         # 입력한 id가 학생의 id 인 경우
        #     id = Student.objects.get(st_id=id_)                      # 입력한 id가 학생의 id 아닌 경우
        # except:
        try:                                                     # 입력한 id가 교수의 id 인 경우
            id     = Professor.objects.get(pro_id=id_)
            id_who = "pro"
        except:                                                  # 입력한 id가 교수의 id 아닌 경우
            id_who = "INCORRECT ID"                              # => incorrect id
            return render(request, 'rollbook/web_Login_Find_Password.html', {'param': 'soongsil note', 'pw':id_who})


        # 위에서 id 체크했던 것과 같이 name, phone도 동일하게 체크한다.
        try:
            name = Student.objects.get(st_name=name_)
        except:
            try:
                name     = Professor.objects.get(pro_name=name_)
                name_who = "pro"
            except:
                name_who = "INCORRECT NAME"
                return render(request, 'rollbook/web_Login_Find_Password.html', {'param': 'soongsil note', 'pw':name_who})


        try:
            beacon = Student.objects.get(st_phoneNum=beacon_)
        except:
            try:
                beacon     = Professor.objects.get(pro_Beacon=beacon_)
                beacon_who = "pro"
            except:
                beacon_who = "INCORRECT Beacon Number"
                return render(request, 'rollbook/web_Login_Find_Password.html', {'param': 'soongsil note', 'pw':phone_who})
        if id_who == "stu" and name_who == "stu" and beacon_who == "stu":         # 입력한 정보가 학생인 경우
            pw = "비밀번호 : " + id.st_pwd

        elif id_who == "pro" and name_who == "pro" and beacon_who == "pro":       # 입력한 정보가 교수인 경우
            pw = "비밀번호 : " + id.pro_pwd

    return render(request, 'rollbook/web_Login_Find_Password.html', {'param': 'soongsil note', 'pw': pw})

@csrf_exempt
def logout(request):                           # 로그아웃 해주는 함수
    return HttpResponseRedirect('/login/')     # 로그인 화면으로 돌려줌

def pro_main(request, pk):                                      # 교수님 메인 화면에 나타날 data를 정리하는 함수

    lectureList_   = Lecture.objects.filter(lec_professor=pk)   # 해당 교수님 교번으로 강의 리스트를 가져온다.
    pro_name_      = Professor.objects.get(pro_id=pk)           # 교수님 이름을 가져온다.

    # try:
    #     pro_name_ = Professor.objects.get(pro_id=pk)
    # except:
    #     pro_name_ = None

    lectures = []
    for i in lectureList_:
        lectures.append(i)

    return render(request, 'rollbook/web_Pro_Main.html', {'pro_name': pro_name_, 'param': 'Professor',
                                                      'semester_lectures': lectures,
                                                      'pro_id': pk})

def pro_lecture_main(request, pk, lec_pk):                        # 교수님 lecture_main에 나올 data를 정리하는 함수
    # if request.session.get('member_id', False) == False:
    #     return HttpResponseRedirect('/login/')

    lecture_ = Lecture.objects.get(lec_id=lec_pk)                 # 해당 강의의 primary key를 넣어서 강의에 대한 정보를 가져온다.
    pro_name_ = Professor.objects.get(pro_id=pk)                  # 교수님 primary key를 넣어서, 교수님 이름 정보를 가져온다

    # if request.method == 'POST':
    #     return render(request, 'blog/pro_lecture_main.html', {'pro_name': pro_name_, 'lecture': lecture_, 'pro_id': pk, 'lec_id': lec_pk, 'param': 'lecture'})

    return render(request, 'rollbook/web_Pro_Lecture_Notice.html', {'pro_name': pro_name_, 'lecture': lecture_, 'pro_id': pk, 'lec_id': lec_pk})

def pro_lecture_managestu_attendance(request, pk, lec_pk):
    # if request.session.get('member_id', False) == False:
    #     return HttpResponseRedirect('/login/')


    # stuinlecs_에 학기 정보를 foreign key로 넣어두면 같은과목 다른학기 일 경우 유용할 듯
    stuinlecs_  = StuInLec.objects.filter(sil_lec=lec_pk)     # 해당 과목을 듣는 학생들을 다 가져와서 넣음
    lecture_    = Lecture.objects.get(lec_id=lec_pk)
    pro_name_   = Professor.objects.get(pro_id=pk)

    # if request.method == 'POST':
    #     if request.POST.get('button')=='save':                                # 저장 버튼을 눌렀을 경우
    #         for i in stuinlecs_:
    #             # score_           = Score.objects.get(sc_stuinlec=i.sil_id)    # 해당 과목을 듣는 학생의 점수를 가져오기
    #
    #             student_         = Student.objects.get(st_id=i.sil_stu.st_id) # 학생 정보 가져오기
    #             attendance_score = request.POST[str(student_.st_id)]          # 내가 입력한 출석점수들을 가져오기
    #
    #             if score_.sc_attendance != attendance_score:                  # 만약 내가 입력한 출석점수와 현재 저장된 출석점수가 다를경우
    #                 query = 'Update blog_score SET sc_attendance="{}" WHERE sc_id="{}"'\
    #                     .format(int(attendance_score), score_.sc_id)          # 출석점수를 update 한다.
    #                 sql(query)
    #
    #     if request.POST.get('button')=='cancel':                              # 취소 버튼을 눌렀을 경우
    #         return redirect('blog.views.pro_lecture_managestu_stulist', pk=pk, lec_pk=lec_pk) # pro_lecture_managestu_stulist로 이동
    #

    a = []
    total_cnt = 0
    a_cnt = 0; l_cnt = 0; n_cnt = 0      #  a_cnt = 출석,  l_cnt = 지각,  n_cnt = 결석

    for i in stuinlecs_:
        lec_attendance = AttendanceRecord.objects.filter(ad_lec=lec_pk, ad_stu=i.sil_stu.st_id)
        # 해당과목 학생의 출석 정보를 가져온다.
        for j in lec_attendance:
            if(j.ad_state == 'Y'):  a_cnt+=1       # 출석횟수를 센다.
            if(j.ad_state == 'L'):  l_cnt+=1       # 지각횟수를 센다.
            if(j.ad_state == 'N'):  n_cnt+=1       # 결석횟수를 센다.

        # attendance_score_ = Score.objects.get(sc_stuinlec=i.sil_id)
        # total_cnt = a_cnt + l_cnt + n_cnt          # 총 수업횟수 = 출석횟수 + 지각횟수 + 결석횟수
        a.append([i.sil_stu.st_id, i.sil_stu.st_name, total_cnt, a_cnt, l_cnt, n_cnt]) # 한 학생의 출석, 지각, 결석횟수
        a_cnt=0; l_cnt=0; n_cnt=0; total_cnt=0

    return render(request, 'rollbook/web_Pro_Lecture_Managestu_Attendance.html',  {'pro_name': pro_name_, 'lecture':lecture_,
                                                                          'pro_id': pk, 'lec_id': lec_pk, 'attendance': a})

def pro_lecture_managestu_attendance_add(request, pk, lec_pk):
    stuinlecs_  = StuInLec.objects.filter(sil_lec=lec_pk)     # 해당 과목을 듣는 학생들을 다 가져와서 넣음
    lecture_    = Lecture.objects.get(lec_id=lec_pk)
    pro_name_   = Professor.objects.get(pro_id=pk)
    astudent_   = stuinlecs_.first().sil_stu


    day_ = []
    date = AttendanceRecord.objects.filter(ad_lec=lec_pk, ad_stu=astudent_)
    for i in date:
        print(i.ad_date)
        day_.append(str(i.ad_date))

    if request.method == 'POST':
        if request.POST.get('button') == 'search':
            stu_add = []
            date_for_check = request.POST.get('select')
            print(date_for_check)
            for i in stuinlecs_:
                stdoftheday = AttendanceRecord.objects.filter(ad_lec=lec_pk, ad_stu=i.sil_stu, ad_date=date_for_check)
                for j in stdoftheday:
                    stu_add.append([i.sil_stu.st_id, i.sil_stu.st_name, j.ad_state, date_for_check])
            return render(request, 'rollbook/web_Pro_Lecture_Managestu_Add.html',
                          {'pro_name': pro_name_, 'lecture': lecture_,
                           'pro_id': pk, 'lec_id': lec_pk, 'daysInMonth': day_, 'attendance': stu_add})

        if request.POST.get('button') == 'save':
            date_for_save = request.POST.get('date')
            for i in stuinlecs_:
                attend = request.POST.get(str(i.sil_stu.st_id))
                try:
                    stdoftheday_save = AttendanceRecord.objects.get(ad_lec=lec_pk, ad_stu=i.sil_stu.st_id, ad_date=date_for_save)
                except:
                    continue

                stdoftheday_save.ad_state = attend
                stdoftheday_save.save()

    # lecture_.lec_name = 'skadua'
    # lecture_.save()



    return render(request, 'rollbook/web_Pro_Lecture_Managestu_Add.html', {'pro_name': pro_name_, 'lecture': lecture_,
                                                                                  'pro_id': pk, 'lec_id': lec_pk, 'daysInMonth': day_})



def pro_lecture_managestu_search(request, pk, lec_pk):
    # # if request.session.get('member_id', False) == False:
    # #     return HttpResponseRedirect('/login/')
    #
    #
    # stuinlecs_에 학기 정보를 foreign key로 넣어두면 같은과목 다른학기 일 경우 유용할 듯
    stuinlecs_  = StuInLec.objects.filter(sil_lec=lec_pk)     # 해당 과목을 듣는 학생들을 다 가져와서 넣음
    lecture_    = Lecture.objects.get(lec_id=lec_pk)
    pro_name_   = Professor.objects.get(pro_id=pk)

    if request.method == 'POST':
        stu_search_name = request.POST.get('stu_name')
        try:
            stu_search_ = Student.objects.filter(st_name=stu_search_name)
        except:
            stu_search_ = None


        a = []
        total_cnt = 0
        a_cnt = 0; l_cnt = 0; n_cnt = 0  # a_cnt = 출석,  l_cnt = 지각,  n_cnt = 결석
        for item in stu_search_ :
            oneStuAboutLec = AttendanceRecord.objects.filter(ad_lec=lec_pk, ad_stu=item)
            for j in oneStuAboutLec:
                if (j.ad_state == 'Y'):  a_cnt += 1  # 출석횟수를 센다.
                if (j.ad_state == 'L'):  l_cnt += 1  # 지각횟수를 센다.
                if (j.ad_state == 'N'):  n_cnt += 1  # 결석횟수를 센다.

            a.append([item.st_id, item.st_name, total_cnt, a_cnt, l_cnt, n_cnt])  # 한 학생의 출석, 지각, 결석횟수
            a_cnt = 0; l_cnt = 0; n_cnt = 0; total_cnt = 0

        return render(request, 'rollbook/web_Pro_Lecture_Managestu_Search.html', {'pro_name': pro_name_, 'lecture': lecture_,
                                                                                  'pro_id': pk, 'lec_id': lec_pk, 'attendance': a})

    return render(request, 'rollbook/web_Pro_Lecture_Managestu_Search.html', {'pro_name': pro_name_, 'lecture': lecture_,
                                                                                  'pro_id': pk, 'lec_id': lec_pk})
