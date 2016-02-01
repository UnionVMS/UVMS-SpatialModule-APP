import os, csv, psycopg2, fileinput

# Definitions
table = 'eez'
schema = 'spatial'
forbidden_fields = ('gid', 'id')
saveFilePath = 'C:\Users\martinhu\Documents\py_scripts\data'

# Go to save target directory
os.chdir(saveFilePath)

# Get a list of fields for the specified table
def getFields(schema, table):
    print 'Step 1 of 3 - Getting table fields...'
    sql = """
        SELECT column_name FROM information_schema.columns
        WHERE table_schema = '{}' and table_name = '{}'
    """.format(schema, table)
    
    cur.execute(sql)
    fields =  [i[0] for i in cur.fetchall()]
    attr = []
    final = {}
    # Remove forbidden fields
    for field in fields:
        if field not in forbidden_fields:
            if field == 'geom' or field == 'the_geom':
                final['geom'] = field;   
            else:
                attr.append(field)
    final['attributes'] = attr
    return final

def exportTableData(schema, table, fields):
    print 'Step 2 of 3 - Getting table data...'
    attrs = ', '.join(fields['attributes'])
    sql = """
        SELECT ST_AsEWKT({}) as {}, {} FROM {}.{}
    """.format(fields['geom'], fields['geom'], attrs, schema, table)
    
    query = 'copy ({0}) to stdout with csv header'.format(sql)
    
    print 'Step 3 of 3 - Writing CSV file...' 
    with open(table + '.csv', 'w') as f:
        cur.copy_expert(query, f)

def fixCsv(table):
    print 'Step 4 of 4 - Fixing WKT delimiters in CSV file...' 
    for line in fileinput.FileInput(table + '.csv',inplace = 1):
        line = line.replace('"', '')
        print line,

def closeConnection():
    cur.close()
    conn.close()
    
# Establish connection to the database
conn = psycopg2.connect("dbname='uvms' user='postgres' password='postgres' host='localhost'")
cur = conn.cursor()

# Do the stuff
fields = getFields(schema, table)
exportTableData(schema, table, fields)
closeConnection()
print 'Export finished!!'.upper()
#fixCsv(table)


