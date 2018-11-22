dict_filepath = 'D:\\Users\\Nirin\\Desktop\\edict\\edict'
input_filepath_template = 'D:\\Users\\Nirin\\Desktop\\N1\\0{0}.txt'
output_filepath_template = 'D:\\Users\\Nirin\\Desktop\\N1\\output\\0{0}.txt'
kd = {} 

for line in open(dict_filepath, 'r', encoding='euc_jp'):  
    words = line.split(' ', 2)
#    for w in words:
#        print(w)
        
    kanji = words[0]
    hiragana = words[1]
    meaning = words[2]
    
    kd[kanji] = hiragana + '|' + meaning


for n in range(1,9):
    input_filepath = input_filepath_template.format(n)
    output_filepath = output_filepath_template.format(n)
    f = open(output_filepath, 'w', encoding='utf8') 
    
    for line in open(input_filepath, 'r', encoding='utf8'):  
        kanji = line.replace('\n', '')
        
        if kanji in kd:
            words = kd[kanji].split('|')
            hiragana = words[0].replace('[', '').replace(']', '')
            meaning = words[1]
            print(kanji + '\t' + hiragana + '\t' + meaning)
            f.write(kanji + '\t' + hiragana + '\t' + meaning)
        else:
            print(kanji)
            f.write(kanji)