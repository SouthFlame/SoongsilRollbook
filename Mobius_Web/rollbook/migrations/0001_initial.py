# -*- coding: utf-8 -*-
# Generated by Django 1.10.3 on 2016-11-26 07:05
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='AttendanceRecord',
            fields=[
                ('ad_id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('ad_date', models.DateField(auto_now_add=True)),
                ('ad_state', models.CharField(default='N', max_length=2)),
            ],
        ),
        migrations.CreateModel(
            name='Lecture',
            fields=[
                ('lec_id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('lec_name', models.CharField(max_length=50)),
                ('lec_access', models.CharField(max_length=10)),
                ('lec_room', models.CharField(max_length=50, null=True)),
                ('lec_time', models.CharField(max_length=50, null=True)),
                ('lec_motion', models.CharField(max_length=50, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='Professor',
            fields=[
                ('pro_id', models.PositiveSmallIntegerField(primary_key=True, serialize=False)),
                ('pro_pwd', models.CharField(max_length=15)),
                ('pro_name', models.CharField(max_length=30)),
                ('pro_isPro', models.BooleanField(default=True)),
                ('pro_Beacon', models.CharField(max_length=40)),
                ('pro_limTime', models.PositiveSmallIntegerField(default=180)),
                ('pro_startTime', models.DateField(auto_now_add=True)),
                ('pro_ipAddr', models.CharField(max_length=30)),
            ],
        ),
        migrations.CreateModel(
            name='Student',
            fields=[
                ('st_id', models.PositiveSmallIntegerField(primary_key=True, serialize=False)),
                ('st_pwd', models.CharField(max_length=15)),
                ('st_name', models.CharField(max_length=30)),
                ('st_isPro', models.BooleanField(default=False)),
                ('st_ipAddr', models.CharField(max_length=30)),
            ],
        ),
        migrations.CreateModel(
            name='StuInLec',
            fields=[
                ('sil_id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('sil_lec', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rollbook.Lecture')),
                ('sil_stu', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rollbook.Student')),
            ],
        ),
        migrations.AddField(
            model_name='lecture',
            name='lec_professor',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rollbook.Professor'),
        ),
        migrations.AddField(
            model_name='attendancerecord',
            name='ad_lec',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rollbook.Lecture'),
        ),
        migrations.AddField(
            model_name='attendancerecord',
            name='ad_stu',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rollbook.Student'),
        ),
    ]
